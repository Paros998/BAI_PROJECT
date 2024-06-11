package org.bai.security.library.rest;

import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import lombok.NonNull;
import org.apache.commons.lang3.NotImplementedException;
import org.bai.security.library.api.auth.LoginRequest;
import org.bai.security.library.security.permission.checker.DisablePermissionChecking;


@Path("/auth")
public class AuthController {

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @DisablePermissionChecking
    public Void login(final @NonNull @Valid LoginRequest request) {
        throw new NotImplementedException("/login should not be called internally, only viable for form authentication filter");
    }
}
