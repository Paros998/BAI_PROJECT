package org.bai.security.library.security.permission;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.core.SecurityContext;
import org.bai.security.library.common.properties.AppState;
import org.bai.security.library.common.properties.PropertyBasedAppState;
import org.bai.security.library.security.permission.checker.AppPermissionChecker;
import org.bai.security.library.security.permission.checker.MockPermissionChecker;
import org.bai.security.library.security.permission.checker.PermissionChecker;
import org.bai.security.library.security.permission.checker.UserPermissionChecker;

@Singleton
public class PermissionConfiguration {
    @RequestScoped
    private final SecurityContext securityContext;

    @ApplicationScoped
    private final AppState appState;

    @Inject
    public PermissionConfiguration(final SecurityContext securityContext, final @PropertyBasedAppState AppState appState) {
        this.securityContext = securityContext;
        this.appState = appState;
    }

    @Produces
    @AppPermissionChecker
    @RequestScoped
    public PermissionChecker permissionChecker() {
        return switch (this.appState.getAppMode()) {
            case SAFE -> new UserPermissionChecker(this.securityContext);
            case UNSAFE -> new MockPermissionChecker();
        };
    }
}