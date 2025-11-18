package bg.senpai_main.web;

import bg.senpai_main.configs.JwtUtil;
import bg.senpai_main.configs.MemberData;
import bg.senpai_main.dtos.memberDtos.LoginRequest;
import bg.senpai_main.dtos.memberDtos.MemberRegisterDTO;
import bg.senpai_main.dtos.memberDtos.MemberResponseDto;
import bg.senpai_main.dtos.memberDtos.UpdateProfileDto;
import bg.senpai_main.entities.Member;
import bg.senpai_main.services.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid MemberRegisterDTO memberRegisterDTO){
        Member member = memberService.registerMember(memberRegisterDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(member.getId())
                .toUri();


        MemberResponseDto memberResponseDto = MemberResponseDto.memberResponseDto(member);
        return ResponseEntity.created(location).body(memberResponseDto);
    }

    @GetMapping("/member/{id}")
    public ResponseEntity<?> getId(@PathVariable UUID id){
        Member member = memberService.findById(id).orElseThrow(() -> new UsernameNotFoundException("Member not found"));
        MemberResponseDto memberResponseDto = MemberResponseDto.memberResponseDto(member);
        return ResponseEntity.ok(memberResponseDto);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMe(@AuthenticationPrincipal MemberData memberData){
        Member member = memberService.findById(memberData.getId()).orElseThrow(() -> new UsernameNotFoundException("Member not found"));
        MemberResponseDto memberResponseDto = MemberResponseDto.memberResponseDto(member);
        return ResponseEntity.ok(memberResponseDto);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateProfile(@AuthenticationPrincipal MemberData memberData, @RequestBody @Valid UpdateProfileDto dto) {
        memberService.updateProfile(memberData.getId(), dto);
        return ResponseEntity.ok("Profile updated successfully!");
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        return ResponseEntity.status(HttpStatus.OK).body(
                Map.of(
                        "token", jwtUtil.generateToken(loginRequest.getUsername()),
                        "member", MemberResponseDto.memberResponseDto(((MemberData) authentication.getPrincipal()).getMember())
                )
        );
    }


    @PostMapping("/profilePicture")
    public ResponseEntity<String> uploadProfilePicture(@AuthenticationPrincipal MemberData user, @RequestParam("file") MultipartFile file) {

        try {
            String uploadDir = "uploads/profile-pictures/";
            Files.createDirectories(Paths.get(uploadDir));

            String fileName = user.getId() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir + fileName);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Създаваме URL за снимката
            String url = "http://localhost:8080/profile-pictures/" + fileName;
            memberService.uploadPfp(url, user.getId());
            return ResponseEntity.ok(url);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("File upload error");
        }
    }

    @GetMapping("/profilePicture")
    public ResponseEntity<String> uploadProfilePicture(@AuthenticationPrincipal MemberData user) {
        return ResponseEntity.ok(memberService.pFpUrl(user.getId()));
    }

}
