package bg.senpai_main.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    private static final String SERVICE_METHODS = "execution(* bg.senpai_main.services.*.*(..))";

    @Before(SERVICE_METHODS)
    public void logBefore(JoinPoint joinPoint) {

        String username = resolveUsername();

        log.info("➡️ [{}] Calling: {} with args: {}",
                username,
                joinPoint.getSignature().toShortString(),
                joinPoint.getArgs()
        );
    }

    @AfterReturning(pointcut = SERVICE_METHODS)
    public void logAfterSuccess(JoinPoint joinPoint) {

        String username = resolveUsername();

        log.info("✔️ [{}] Success: {}",
                username,
                joinPoint.getSignature().toShortString()
        );
    }

    @AfterThrowing(pointcut = SERVICE_METHODS, throwing = "ex")
    public void logAfterError(JoinPoint joinPoint, Throwable ex) {

        String username = resolveUsername();

        log.error("❌ [{}] Error in {} – {}",
                username,
                joinPoint.getSignature().toShortString(),
                ex.getMessage()
        );
    }

    private String resolveUsername() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            return (auth != null) ? auth.getName() : "anonymous";
        } catch (Exception e) {
            return "unknown";
        }
    }
}
