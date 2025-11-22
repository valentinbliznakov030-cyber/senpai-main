package bg.senpai_main.services;

import bg.senpai_main.configs.MemberData;
import bg.senpai_main.dtos.adminDtos.AdminMemberUpdateDto;
import bg.senpai_main.dtos.memberDtos.MemberRegisterDTO;
import bg.senpai_main.dtos.memberDtos.UpdateProfileDto;
import bg.senpai_main.entities.Member;
import bg.senpai_main.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MemberService {

    Member registerMember(MemberRegisterDTO registerDTO);

    Optional<Member> findByEmail(String email);

    Optional<Member> findByUsername(String username);

    Optional<Member> findById(UUID memberId);

    Page<Member> findAll(int page, int size);

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

    void delete(MemberData memberData, String imageName);

    Member updateMemberByAdmin(AdminMemberUpdateDto adminMemberUpdateDto);

    void adminDeleteProfilePicture(UUID memberId, String imageName);

    ResponseEntity<String> adminUploadImage(UUID memberId, MultipartFile file);

    Member changePassword(String email, String password);
}
