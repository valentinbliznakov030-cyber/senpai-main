package bg.senpai_main.services.impl;

import bg.senpai_main.dtos.memberDtos.MemberRegisterDTO;
import bg.senpai_main.dtos.memberDtos.UpdateProfileDto;
import bg.senpai_main.entities.Member;
import bg.senpai_main.enums.Role;
import bg.senpai_main.exceptions.EntityNotFoundException;
import bg.senpai_main.repositories.MemberRepository;
import bg.senpai_main.services.MemberService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

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
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    @Override
    public Member changeRole(UUID memberId, Role newRole) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("Member not found!"));
        member.setRole(newRole);
        return memberRepository.save(member);
    }

    @Override
    public void toggleActive(UUID memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("Member not found!"));
        member.setActive(!member.isActive());
        memberRepository.save(member);
    }

    @Override
    public void deleteMember(UUID memberId) {
        memberRepository.deleteById(memberId);
    }


    @Override
    public boolean deleteAll() {
        try{
            memberRepository.deleteAll();
            return true;
        } catch (Exception e) {
            throw e;
        }
    }


    @Override
    public String pFpUrl(UUID memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("Member not found"));
        return member.getProfilePictureUrl();
    }


    @Override
    public void uploadPfp(String url, UUID memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("member not found"));
        member.setProfilePictureUrl(url);
        memberRepository.save(member);
    }

    @Override
    @Transactional
    public void updateProfile(UUID memberId, UpdateProfileDto dto) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));

        // 🔥 CHECK USERNAME UNIQUE
        if (dto.getUsername() != null && !dto.getUsername().isBlank()) {

            // ако username е различен от текущия
            if (!dto.getUsername().equalsIgnoreCase(member.getUsername())) {

                boolean exists = memberRepository.existsByUsername(dto.getUsername());
                if (exists) {
                    throw new IllegalArgumentException("Username is already taken.");
                }

                member.setUsername(dto.getUsername());
            }
        }

        // 🔥 CHECK EMAIL UNIQUE
        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {

            if (!dto.getEmail().equalsIgnoreCase(member.getEmail())) {

                boolean exists = memberRepository.existsByEmail(dto.getEmail());
                if (exists) {
                    throw new IllegalArgumentException("Email is already in use.");
                }

                member.setEmail(dto.getEmail());
            }
        }

        memberRepository.save(member);
    }


    @Override
    public List<Member> findFilteredMembers(String username, String email, Boolean active, Role role, LocalDateTime registeredOn) {

        return memberRepository.findAll(((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (username != null && !username.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("username"), username));
            }

            if (email != null && !email.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("email"), email));
            }

            if (active != null) {
                predicates.add(criteriaBuilder.equal(root.get("active"), active));
            }

            if (role != null) {
                predicates.add(criteriaBuilder.equal(root.get("role"), role));
            }

            if (registeredOn != null) {
                predicates.add(criteriaBuilder.equal(root.get("registeredOn"), registeredOn));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));

        }));
    }
}
