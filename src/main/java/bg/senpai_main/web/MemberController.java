package bg.senpai_main.web;

import bg.senpai_main.configs.JwtUtil;
import bg.senpai_main.dtos.memberDtos.LoginRequest;
import bg.senpai_main.dtos.memberDtos.MemberRegisterDTO;
import bg.senpai_main.dtos.memberDtos.MemberResponseDto;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/member")
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
    @PreAuthorize("hasRole('ADMIN') or authentication.principal.id == #id")
    public ResponseEntity<?> getId(@PathVariable UUID id){
        return ResponseEntity.ok(memberService.findById(id).orElseThrow(() -> new UsernameNotFoundException("Member not found")));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));


        return ResponseEntity.status(HttpStatus.OK).body(jwtUtil.generateToken(loginRequest.getUsername()));
    }
}
