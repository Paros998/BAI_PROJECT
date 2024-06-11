package org.bai.security.library.rest;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.bai.security.library.api.lends.LendDto;
import org.bai.security.library.domain.lend.LendRepository;
import org.bai.security.library.security.permission.checker.PermissionChecker;

import java.util.List;
import java.util.UUID;

@Path("book-lends")
public class LendController {
    private final LendRepository lendRepository;
    private final PermissionChecker userPermissionChecker;

    @Inject
    public LendController(final LendRepository lendRepository,
                          final PermissionChecker userPermissionChecker) {
        this.lendRepository = lendRepository;
        this.userPermissionChecker = userPermissionChecker;
    }

    @GET
    @Path("/find/{userId}")
    @Produces(value = MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN", "USER"})
    public List<LendDto> findUserLends(final @PathParam("userId") UUID userId) {
        userPermissionChecker.check();
        return lendRepository.findAllByUser(userId);
    }

    @POST
    @Path("/new/{bookId}/{userId}")
    @Produces(value = MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN", "USER"})
    public UUID lendBook(final @PathParam("bookId") UUID bookId, final @PathParam("userId") UUID userId) {
        userPermissionChecker.check();
        return lendRepository.lendBook(userId, bookId);
    }
}