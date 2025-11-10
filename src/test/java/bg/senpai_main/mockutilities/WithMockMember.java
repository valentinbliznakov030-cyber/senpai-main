package bg.senpai_main.mockutilities;

import bg.senpai_main.enums.Role;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockMemberSecurityContextFactory.class)
public @interface WithMockMember {
    String username() default "valkata";
    String password() default "encoded123Password";
    String uuid() default "a1e3f8b0-4d5c-4e67-9c2b-3a0f5e1b7c8d";
    String email() default "valkata@senpai.bg";
    Role role() default Role.USER;
}
