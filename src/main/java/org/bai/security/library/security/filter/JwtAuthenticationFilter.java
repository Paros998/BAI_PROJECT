package org.bai.security.library.security.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.lang.Strings;
import io.jsonwebtoken.security.Keys;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.bai.security.library.api.common.HttpStatusError;
import org.bai.security.library.common.properties.AppProperties;
import org.bai.security.library.security.context.UserPrincipal;
import org.bai.security.library.security.context.UserSecurityContext;
import org.bai.security.library.security.jwt.parser.JwtBodyParser;
import org.bai.security.library.security.jwt.parser.JwtBodyParserService;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Slf4j
@ApplicationScoped
public class JwtAuthenticationFilter implements ContainerRequestFilter {
    private static final String JWT_SCHEME = "JWT";

    private final JwtBodyParserService bodyParserService;

    @Inject
    public JwtAuthenticationFilter(final @JwtBodyParser JwtBodyParserService bodyParserService) {
        this.bodyParserService = bodyParserService;
    }

    @Override
    public void filter(final @NonNull ContainerRequestContext requestContext) {
        String authorizationHeader = requestContext.getHeaderString("Authorization");

        if (!Objects.isNull(authorizationHeader) && Strings.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.replace("Bearer ", "");
            try {
                final String jwtSecret = AppProperties.getProperties().getJwt().getSecret();
                final Jws<Claims> claimsJws = Jwts.parserBuilder()
                        .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8))).build()
                        .parseClaimsJws(token);
                final Claims body = claimsJws.getBody();

                final UserPrincipal principal = bodyParserService.getUserPrincipal(body);

                final var context = UserSecurityContext.builder()
                        .principal(principal)
                        .isSecure(true)
                        .authScheme(JWT_SCHEME)
                        .build();

                requestContext.setSecurityContext(context);

            } catch (final ExpiredJwtException e) {
                log.error("Token {} has expired, error", token, e);
                requestContext.abortWith(Response
                        .status(HttpStatusError.FORBIDDEN.status())
                        .entity(HttpStatusError.FORBIDDEN.msg())
                        .build()
                );
            } catch (final Exception e) {
                log.error("Token {} cannot be trusted, error", token, e);
                requestContext.abortWith(Response
                        .status(HttpStatusError.SECURITY_TOKEN_UNTRUSTED.status())
                        .entity(HttpStatusError.SECURITY_TOKEN_UNTRUSTED.msg())
                        .build()
                );
            }
        }
    }
}
