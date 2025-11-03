package bg.senpai_main.configs;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);

            try {
                Claims claims = jwtUtil.extractClaims(token);
                username = jwtUtil.extractUsername(claims);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    var userDetails = userDetailsService.loadUserByUsername(username);

                    if (jwtUtil.isValidToken(claims, username)) {
                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails, null, userDetails.getAuthorities());
                        authToken.setDetails(
                                new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }

            } catch (io.jsonwebtoken.ExpiredJwtException e) {
                sendError(response, "JWT expired", "JWT_EXPIRED", 401);
                return;

            } catch (io.jsonwebtoken.security.SignatureException e) {
                sendError(response, "Invalid JWT signature", "JWT_INVALID_SIGNATURE", 401);
                return;

            } catch (io.jsonwebtoken.MalformedJwtException e) {
                sendError(response, "Malformed JWT token", "JWT_MALFORMED", 401);
                return;

            } catch (Exception e) {
                sendError(response, "Invalid or expired JWT token", "JWT_INVALID", 401);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private void sendError(HttpServletResponse response, String message, String code, int status) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");

        String json = String.format("""
        {
            "error": "%s",
            "code": "%s",
            "status": %d,
            "timestamp": "%s"
        }
    """, message, code, status, java.time.LocalDateTime.now());

        response.getWriter().write(json);
    }

}
