package org.bai.security.library.security.permission;

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

@RequestScoped
public class UserPermissionChecker {

    @Inject
    @RequestScoped
    private SecurityContext securityContext;

    public void check() {
        try {
            StackTraceElement callable = Thread.currentThread().getStackTrace()[3];
            String callableMethodName = callable.getMethodName();
            String callableClassName = callable.getClassName();

            Class clazz = Class.forName(callableClassName);

            Method calledMethod = clazz.getMethod(callableMethodName);

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

                rolesNeeded.forEach(role -> {
                    if (!securityContext.isUserInRole(role)) {
                        throw new WebApplicationException(
                                Response.status(
                                                HttpStatusError.FORBIDDEN.status(),
                                                String.format("User doesn't have the required permission [%s],denying access.", role))
                                        .build()
                        );
                    }
                });
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