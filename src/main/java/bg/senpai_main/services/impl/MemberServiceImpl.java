package bg.senpai_main.services.impl;

import bg.senpai_main.configs.MemberData;
import bg.senpai_main.dtos.adminDtos.AdminMemberUpdateDto;
import bg.senpai_main.dtos.memberDtos.MemberRegisterDTO;
import bg.senpai_main.dtos.memberDtos.MemberResponseDto;
import bg.senpai_main.dtos.memberDtos.UpdateProfileDto;
import bg.senpai_main.entities.Member;
import bg.senpai_main.enums.Role;
import bg.senpai_main.exceptions.EntityNotFoundException;
import bg.senpai_main.exceptions.ImageNotDeletedException;
import bg.senpai_main.exceptions.ImageNotUplaodedException;
import bg.senpai_main.repositories.MemberRepository;
import bg.senpai_main.services.MemberService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // ======================
    // REGISTRATION
    // ======================
    @Override
    public Member registerMember(MemberRegisterDTO dto) {
        if (memberRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists!");
        }
        if (memberRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists!");
        }

        Member member = Member.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(Role.USER)
                .active(true)
                .registeredOn(LocalDateTime.now())
                .build();

        return memberRepository.save(member);
    }

    // ======================
    // BASIC FINDERS (NO CACHE)
    // ======================
    @Override
    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    @Override
    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    @Override
    public Optional<Member> findById(UUID id) {
        return memberRepository.findById(id);
    }

    @Override
    public Page<Member> findAll(int page, int size) {
        return memberRepository.findAll(PageRequest.of(page, size));
    }

    // ======================
    // READ-ONLY DTO (CACHED)
    // ======================
    @Override
    @Cacheable(value = "memberDto", key = "#id")
    public MemberResponseDto getMemberDto(UUID id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));

        return MemberResponseDto.memberResponseDto(member);
    }

    // ======================
    // UPDATE METHODS (CACHE EVICT)
    // ======================
    @Override
    @CacheEvict(value = "memberDto", key = "#memberId")
    public Member changeRole(UUID memberId, Role newRole) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("Member not found!"));
        member.setRole(newRole);
        return memberRepository.save(member);
    }

    @Override
    @CacheEvict(value = "memberDto", key = "#memberId")
    public void toggleActive(UUID memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("Member not found!"));
        member.setActive(!member.isActive());
        memberRepository.save(member);
    }

    @Override
    @CacheEvict(value = "memberDto", key = "#memberId")
    public void deleteMember(UUID memberId) {
        memberRepository.deleteById(memberId);
    }

    @Override
    public boolean deleteAll() {
        memberRepository.deleteAll();
        return true;
    }

    // ======================
    // PROFILE PICTURE
    // ======================
    @Override
    @CacheEvict(value = "memberDto", key = "#memberId")
    public void uploadPfp(String url, UUID memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));
        member.setProfilePictureUrl(url);
        memberRepository.save(member);
    }

    @Override
    public String pFpUrl(UUID memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));
        return member.getProfilePictureUrl();
    }

    @Override
    public List<Member> findFilteredMembers(String username,
                                            String email,
                                            Boolean active,
                                            Role role,
                                            LocalDateTime registeredOn) {

        return memberRepository.findAll((root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (username != null && !username.isEmpty()) {
                predicates.add(builder.equal(root.get("username"), username));
            }

            if (email != null && !email.isEmpty()) {
                predicates.add(builder.equal(root.get("email"), email));
            }

            if (active != null) {
                predicates.add(builder.equal(root.get("active"), active));
            }

            if (role != null) {
                predicates.add(builder.equal(root.get("role"), role));
            }

            if (registeredOn != null) {
                predicates.add(builder.equal(root.get("registeredOn"), registeredOn));
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        });
    }
    @Override
    public void delete(MemberData memberData, String imageName) {
        String uploadDir = "uploads/profile-pictures/";
        String fileName = memberData.getId() + "_" + imageName;

        Path filePath = Paths.get(uploadDir, fileName);

        try {
            boolean deleted = Files.deleteIfExists(filePath);

            if (!deleted) {
                throw new ImageNotDeletedException("Profile picture does not exist.");
            }

        } catch (Exception e) {
            throw new ImageNotDeletedException(e.getMessage());
        }
    }

    // ======================
    // PROFILE UPDATE
    // ======================
    @Override
    @CacheEvict(value = "memberDto", key = "#memberId")
    public void updateProfile(UUID memberId, UpdateProfileDto dto) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));

        if (dto.getUsername() != null && !dto.getUsername().isBlank()) {
            if (!dto.getUsername().equalsIgnoreCase(member.getUsername()) &&
                    memberRepository.existsByUsername(dto.getUsername())) {
                throw new IllegalArgumentException("Username already taken.");
            }
            member.setUsername(dto.getUsername());
        }

        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            if (!dto.getEmail().equalsIgnoreCase(member.getEmail()) &&
                    memberRepository.existsByEmail(dto.getEmail())) {
                throw new IllegalArgumentException("Email already in use.");
            }
            member.setEmail(dto.getEmail());
        }

        memberRepository.save(member);
    }


    // ======================
    // ADMIN UPDATE
    // ======================
    @Override
    @CacheEvict(value = "memberDto", key = "#dto.id")
    public Member updateMemberByAdmin(AdminMemberUpdateDto dto) {

        Member member = memberRepository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));

        dto.getUsername().ifPresent(member::setUsername);
        dto.getEmail().ifPresent(member::setEmail);
        dto.getActive().ifPresent(member::setActive);
        dto.getRole().ifPresent(member::setRole);

        return memberRepository.save(member);
    }

    @Override
    public void adminDeleteProfilePicture(UUID memberId, String imageName) {
        Path uploadDir = Paths.get("uploads", "profile-pictures").toAbsolutePath();
        Path filePath = uploadDir.resolve(memberId + "_" + imageName);

        try {
            boolean deleted = Files.deleteIfExists(filePath);
            if (!deleted) {
                throw new ImageNotDeletedException("Profile picture not found.");
            }

        } catch (Exception e) {
            throw new ImageNotDeletedException("Could not delete profile picture: " + e.getMessage());
        }
    }

    // ======================
    // ADMIN UPLOAD IMAGE
    // ======================
    public ResponseEntity<String> adminUploadImage(UUID memberId, MultipartFile file) {
        try {
            String uploadDir = "uploads/profile-pictures/";
            Files.createDirectories(Paths.get(uploadDir));

            String fileName = memberId + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir + fileName);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            String url = "http://localhost:8080/profile-pictures/" + fileName;

            uploadPfp(url, memberId);

            return ResponseEntity.ok(url);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("File upload error: " + e.getMessage());
        }
    }

    // ======================
    // CHANGE PASSWORD
    // ======================
    @CacheEvict(value = "memberDto", key = "#member.id")
    public Member changePassword(String email, String password) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));

        member.setPassword(passwordEncoder.encode(password));
        return memberRepository.save(member);
    }

}
