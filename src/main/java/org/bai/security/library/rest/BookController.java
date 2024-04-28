package org.bai.security.library.rest;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.NonNull;
import org.bai.security.library.api.book.BookDto;
import org.bai.security.library.business.BusinessExceptionFactory;
import org.bai.security.library.domain.book.BookRepository;
import org.bai.security.library.security.permission.checker.AppPermissionChecker;
import org.bai.security.library.security.permission.checker.PermissionChecker;

import java.util.List;
import java.util.UUID;

@Path("books")
public class BookController {
    private final BookRepository bookRepository;
    private final PermissionChecker userPermissionChecker;

    @Inject
    public BookController(final BookRepository bookRepository,
                          final @AppPermissionChecker PermissionChecker permissionChecker) {
        this.bookRepository = bookRepository;
        this.userPermissionChecker = permissionChecker;
    }

    @GET
    @Path("/find")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN", "USER"})
    public List<BookDto> getAllBooks() {
        userPermissionChecker.check();
        return bookRepository.findAll();
    }

    @GET
    @Path("/find/{bookId}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN", "USER"})
    public BookDto findBookById(final @NonNull @PathParam("bookId") UUID bookId) {
        userPermissionChecker.check();
        return bookRepository.findById(bookId)
                .orElseThrow(() -> BusinessExceptionFactory.forMessage(String.format("Book with id:[%s] not found", bookId)));
    }

    @POST
    @Path("/new")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("ADMIN")
    public UUID addNewBook(final @NonNull @Valid BookDto book) {
        return bookRepository.addBook(book);
    }
}