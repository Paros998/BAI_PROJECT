package org.bai.security.library.rest;

import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.NonNull;
import org.apache.commons.lang3.NotImplementedException;
import org.bai.security.library.api.users.LoginRequest;


@Path("/auth")
public class AuthResource {

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Void login(final @NonNull @Valid LoginRequest request) {
        throw new NotImplementedException("/login should not be called internally, only viable for form authentication filter");
    }
}
