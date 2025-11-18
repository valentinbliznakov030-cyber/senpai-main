package bg.senpai_main.services;

import bg.senpai_main.dtos.memberDtos.MemberRegisterDTO;
import bg.senpai_main.dtos.memberDtos.UpdateProfileDto;
import bg.senpai_main.entities.Member;
import bg.senpai_main.enums.Role;

import java.time.LocalDateTime;
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

    String pFpUrl(UUID memberId);

    void uploadPfp(String pfpUrl, UUID memberId);

    void updateProfile(UUID id, UpdateProfileDto dto);

    List<Member> findFilteredMembers(String username,
                                     String email,
                                     Boolean active,
                                     Role role,
                                     LocalDateTime registeredOn);
}
