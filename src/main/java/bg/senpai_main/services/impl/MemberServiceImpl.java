package bg.senpai_main.services.impl;

import bg.senpai_main.dtos.memberDtos.MemberRegisterDTO;
import bg.senpai_main.entities.Member;
import bg.senpai_main.enums.Role;
import bg.senpai_main.repositories.MemberRepository;
import bg.senpai_main.services.MemberService;
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
}
