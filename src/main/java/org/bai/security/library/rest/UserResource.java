package org.bai.security.library.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import lombok.NonNull;
import org.bai.security.library.api.users.UserDto;
import org.bai.security.library.domain.user.UserRepository;
import org.bai.security.library.entity.user.UserJdbcRepository;

import java.util.List;
import java.util.UUID;

@Path("/users")
public class UserResource {
    private final UserRepository userRepository;

    @Inject
    public UserResource(final UserJdbcRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GET
    @Path("")
    @Produces({"application/json"})
    public List<UserDto> getAllUsers() {
        return userRepository.findByAll();
    }

    @GET
    @Path("/find/{userId}")
    @Produces({"application/json"})
    public UserDto getUserById(final @NonNull @PathParam("userId") UUID userId) {
        return userRepository.findById(userId).orElseThrow();
    }
}
