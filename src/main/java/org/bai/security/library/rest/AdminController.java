package org.bai.security.library.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.UUID;
import org.bai.security.library.domain.user.UserRepository;

@Path("/admin")
public class AdminController {
  private final UserRepository userRepository;

  @Inject
  public AdminController(final UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @GET
  @Path("/delete-user/{userId}")
  @Produces(MediaType.TEXT_PLAIN)
  public String deleteUser(@PathParam("userId") UUID userId) {
    // No admin privileges check
    return userRepository.deleteById(userId) ? "User deleted." : "User not found.";
  }
}
