package org.bai.security.library.business;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;

import static org.bai.security.library.api.common.HttpStatusError.FORBIDDEN;

@RequestScoped
public class UserRolePermissionChecker {
    @Inject
    @RequestScoped
    private SecurityContext context;

    @Inject
    @RequestScoped
    private HttpServletResponse response;

    public void checkPermission(final @NonNull UserRole roleToCheck) {
        try {
            if (!context.isCallerInRole(roleToCheck.name())) {
                response.sendError(FORBIDDEN.status(), FORBIDDEN.msg());
            }
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
}