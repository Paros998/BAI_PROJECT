package org.bai.security.library.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.bai.security.library.domain.user.UserRepository;
import org.bai.security.library.entity.user.repository.UserEntityRepository;

import java.util.UUID;

@Path("/admin")
public class AdminController {
    private final UserRepository userRepository;

    @Inject
    public AdminController(final @UserEntityRepository UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GET
    @Path("/delete-user/{userId}")
    public String deleteUser(@PathParam("userId") UUID userId) {
        // No admin privileges check
        return userRepository.deleteById(userId) ? "User deleted." : "User not found.";
    }
}
