package org.bai.security.library.security.filter;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.inject.Inject;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import lombok.NonNull;
import org.bai.security.library.api.common.HttpStatusError;
import org.bai.security.library.security.context.UserPrincipal;
import org.bai.security.library.security.DataSourceIdentityStore;
import org.bai.security.library.security.jwt.JwtExpire;
import org.glassfish.jersey.server.ContainerRequest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class FormAuthenticationFilter implements ContainerRequestFilter {
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String LOGIN_PATH = "/auth/login";

    // temporary
    private static final String JWT_SECRET = "absfol7814lqmva7891294nn9asa783nr293urnq9adm38ry2";

    private final DataSourceIdentityStore identityStore;

    @Inject
    public FormAuthenticationFilter(final DataSourceIdentityStore identityStore) {
        this.identityStore = identityStore;
    }

    @Override
    public void filter(final @NonNull ContainerRequestContext requestContext) throws IOException {
        if (Objects.nonNull(requestContext.getSecurityContext().getUserPrincipal())) {
            // already authenticated, no need to check more
            return;
        }

        String urlPath = ((ContainerRequest) requestContext).getPath(true);

        if (Objects.equals(urlPath, LOGIN_PATH) && requestContext.getMethod().equals("POST")) {

            byte[] entity = requestContext.getEntityStream().readAllBytes();

            List<String> data = new ArrayList<>(List.of(new String(entity).split("&")));

            Map<String, String> parameters = data.stream()
                    .collect(HashMap::new,
                            (m, param) -> {
                                String[] paramData = param.split("=");
                                m.put(paramData[0], paramData[1]);
                            },
                            HashMap::putAll);

            String username = parameters.get(USERNAME);
            username = username != null ? username : "";
            username = username.trim();

            String password = parameters.get(PASSWORD);
            password = password != null ? password : "";

            UsernamePasswordCredential credential = new UsernamePasswordCredential(username, password);

            CredentialValidationResult result = identityStore.validate(credential);

            if (result.getStatus() != CredentialValidationResult.Status.VALID) {
                requestContext.abortWith(
                        Response.status(HttpStatusError.UNAUTHORIZED.status())
                                .entity("Invalid username or password.")
                                .build());
                return;
            }

            UserPrincipal principal = (UserPrincipal) result.getCallerPrincipal();

            try {
                String accessToken = Jwts.builder()
                        .setSubject(principal.getUsername())
                        .claim("authorities", principal.getRoles())
                        .claim("userId", principal.getId())
                        .setIssuedAt(new Date())
                        .setExpiration(new Date(System.currentTimeMillis() + JwtExpire.ACCESS_TOKEN.getAmount()))
                        .signWith(Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8)))
                        .compact();

                requestContext.abortWith(Response.ok()
                        .header("Authorization", "Bearer " + accessToken)
                        .build());
            } catch (final Exception e) {
                requestContext.abortWith(Response.serverError().entity(e.getMessage()).build());
            }
        }
    }
}