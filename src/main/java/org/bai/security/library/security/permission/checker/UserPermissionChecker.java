package org.bai.security.library.security.permission.checker;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.bai.security.library.api.common.HttpStatusError;

import java.lang.reflect.Method;
import java.security.Principal;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class UserPermissionChecker implements PermissionChecker {

    @RequestScoped
    private final SecurityContext securityContext;

    @Inject
    public UserPermissionChecker(final SecurityContext securityContext) {
        this.securityContext = securityContext;
    }

    @Override
    public void check() {
        try {
            Method calledMethod = null;
            StackTraceElement callable = Thread.currentThread().getStackTrace()[3];
            String callableMethodName = callable.getMethodName();
            String callableClassName = callable.getClassName();

            Class<?> clazz = Class.forName(callableClassName);

            Method[] declaredMethods = clazz.getDeclaredMethods();

            for (var m: declaredMethods) {
                if (m.getName().equals(callableMethodName)) {
                    calledMethod = m;
                }
            }

            if (calledMethod == null) {
                throw new WebApplicationException();
            }

            RolesAllowed rolesAllowed = calledMethod.getAnnotation(RolesAllowed.class);

            if (rolesAllowed != null) {
                Set<String> rolesNeeded = Arrays.stream(rolesAllowed.value()).collect(Collectors.toSet());

                Principal userPrincipal = securityContext.getUserPrincipal();
                if (userPrincipal == null) {
                    throw new WebApplicationException(
                            Response.status(
                                            HttpStatusError.FORBIDDEN.status(),
                                            "Couldn't check permissions, user not present in context, denying access.")
                                    .build()
                    );
                }

                if (rolesNeeded.stream().noneMatch(securityContext::isUserInRole)) {
                    throw new WebApplicationException(
                            Response.status(
                                            HttpStatusError.FORBIDDEN.status(),
                                            String.format("User doesn't have one of the required permission [%s], denying access.",
                                                    rolesNeeded))
                                    .build()
                    );
                }

            }
        } catch (final WebApplicationException e) {
            throw e;
        } catch (final Exception e) {
            throw new WebApplicationException(
                    Response.status(
                                    HttpStatusError.FORBIDDEN.status(),
                                    "Couldn't check permissions, denying access.")
                            .build()
            );
        }
    }
}