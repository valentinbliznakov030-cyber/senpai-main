package bg.senpai_main.services;

import bg.senpai_main.dtos.memberDtos.MemberRegisterDTO;
import bg.senpai_main.entities.Member;
import bg.senpai_main.enums.Role;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MemberService {

    Member registerMember(MemberRegisterDTO registerDTO);

    Optional<Member> findByEmail(String email);

    Optional<Member> findByUsername(String username);

    Optional<Member> findById(UUID memberId);

    List<Member> findAll();

    Member changeRole(UUID memberId, Role newRole);

    void toggleActive(UUID memberId);

    void deleteMember(UUID memberId);

    boolean deleteAll();

}
