package org.bai.security.library.security.context;


import jakarta.ws.rs.core.SecurityContext;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.security.Principal;

@Builder
@RequiredArgsConstructor
public class UserSecurityContext implements SecurityContext {
    private final UserPrincipal principal;
    private final String authScheme;
    private final boolean isSecure;

    @Override
    public Principal getUserPrincipal() {
        return principal;
    }

    @Override
    public boolean isUserInRole(String role) {
        return principal.getRoles().contains(role);
    }

    @Override
    public boolean isSecure() {
        return isSecure;
    }

    @Override
    public String getAuthenticationScheme() {
        return authScheme;
    }
}
