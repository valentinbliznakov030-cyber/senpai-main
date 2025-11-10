package bg.senpai_main.mockutilities;

import bg.senpai_main.configs.MemberData;
import bg.senpai_main.entities.Member;
import bg.senpai_main.enums.Role;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;
import java.util.UUID;

public class WithMockMemberSecurityContextFactory implements WithSecurityContextFactory<WithMockMember> {
    @Override
    public SecurityContext createSecurityContext(WithMockMember annotation) {
        String username = annotation.username();
        String password = annotation.password();
        UUID uuid = UUID.fromString(annotation.uuid());
        Role role = annotation.role();

        Member member = Member.builder()
                .role(role)
                .id(uuid)
                .password(password)
                .username(username)
                .build();

        MemberData memberData = new MemberData(member);

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(memberData, null, memberData.getAuthorities());


        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(token);

        return securityContext;
    }
}
