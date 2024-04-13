package org.bai.security.library.security.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.lang.Strings;
import io.jsonwebtoken.security.Keys;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import lombok.NonNull;
import org.bai.security.library.api.common.HttpStatusError;
import org.bai.security.library.security.context.UserPrincipal;
import org.bai.security.library.security.context.UserSecurityContext;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class JwtAuthenticationFilter implements ContainerRequestFilter {
    private static final String JWT_SCHEME = "JWT";
    // temporary
    private static final String JWT_SECRET = "absfol7814lqmva7891294nn9asa783nr293urnq9adm38ry2";

    @Override
    public void filter(final @NonNull ContainerRequestContext requestContext) {
        String authorizationHeader = requestContext.getHeaderString("Authorization");

        if (!Objects.isNull(authorizationHeader) && Strings.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.replace("Bearer ", "");
            try {

                Jws<Claims> claimsJws = Jwts.parserBuilder()
                        .setSigningKey(Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8))).build()
                        .parseClaimsJws(token);
                Claims body = claimsJws.getBody();

                String userId = (String) body.get("userId");
                String username = body.getSubject();

                Set<String> authorities = new HashSet<>((List<String>) body.get("authorities"));

                final UserPrincipal principal = new UserPrincipal(userId, username, true, authorities);

                final var context = UserSecurityContext.builder()
                        .principal(principal)
                        .isSecure(true)
                        .authScheme(JWT_SCHEME)
                        .build();

                requestContext.setSecurityContext(context);

            } catch (final ExpiredJwtException e) {
                requestContext.abortWith(Response
                        .status(HttpStatusError.FORBIDDEN.status())
                        .entity(HttpStatusError.FORBIDDEN.msg())
                        .build()
                );
            } catch (final Exception e) {
                throw new IllegalStateException(String.format("Token %s cannot be trusted", token));
            }
        }
    }
}
