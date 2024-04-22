package org.bai.security.library.rest;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.NonNull;
import org.bai.security.library.api.users.RegisterRequest;
import org.bai.security.library.api.users.UserDto;
import org.bai.security.library.domain.user.UserRepository;
import org.bai.security.library.entity.user.repository.UserEntityRepository;
import org.bai.security.library.security.permission.checker.AppPermissionChecker;
import org.bai.security.library.security.permission.checker.PermissionChecker;

import java.util.List;
import java.util.UUID;

@Path("/users")
public class UserResource {
    private final UserRepository userRepository;
    private final PermissionChecker userPermissionChecker;

    @Inject
    public UserResource(final @UserEntityRepository UserRepository userRepository,
                        final @AppPermissionChecker PermissionChecker permissionChecker) {
        this.userRepository = userRepository;
        this.userPermissionChecker = permissionChecker;
    }

    @GET
    @Path("/find")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("ADMIN")
    public List<UserDto> getAllUsers() {
        userPermissionChecker.check();
        return userRepository.findAll();
    }

    @GET
    @Path("/find/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN", "USER"})
    public UserDto getUserById(final @NonNull @PathParam("userId") UUID userId) {
        userPermissionChecker.check();
        return userRepository.findById(userId).orElseThrow();
    }

    @POST
    @Path("/new")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("ADMIN")
    public UUID createUser(final @NonNull @Valid UserDto userDto) {
        userPermissionChecker.check();
        return userRepository.saveUser(userDto);
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public UUID register(final @NonNull @Valid RegisterRequest request) {
        return userRepository.registerUser(request);
    }

}
