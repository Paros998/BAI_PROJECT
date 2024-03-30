package org.bai.security.library.rest;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.NonNull;
import org.bai.security.library.api.users.UserDto;
import org.bai.security.library.domain.user.UserRepository;
import org.bai.security.library.entity.user.UserEntityRepository;

import java.util.List;
import java.util.UUID;

@Path("/users")
public class UserResource {
    private final UserRepository userRepository;

    @Inject
    public UserResource(final UserEntityRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GET
    @Path("/find")
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserDto> getAllUsers() {
        return userRepository.findByAll();
    }

    @GET
    @Path("/find/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public UserDto getUserById(final @NonNull @PathParam("userId") UUID userId) {
        return userRepository.findById(userId).orElseThrow();
    }

    @POST
    @Path("/new")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public UUID createUser(final @NonNull @Valid UserDto userDto) {
        return userRepository.saveUser(userDto);
    }
}
