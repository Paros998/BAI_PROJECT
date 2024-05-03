package org.bai.security.library.rest;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.NonNull;
import org.bai.security.library.api.stocks.AddBookStockDto;
import org.bai.security.library.api.stocks.BookStockDto;
import org.bai.security.library.domain.stock.BookStockRepository;
import org.bai.security.library.security.permission.checker.AppPermissionChecker;
import org.bai.security.library.security.permission.checker.PermissionChecker;

import java.util.List;
import java.util.UUID;

@Path("book-stocks")
public class BookStockController {
    private final BookStockRepository bookStockRepository;
    private final PermissionChecker userPermissionChecker;


    @Inject
    public BookStockController(final BookStockRepository bookStockRepository,
                               final @AppPermissionChecker PermissionChecker userPermissionChecker) {
        this.bookStockRepository = bookStockRepository;
        this.userPermissionChecker = userPermissionChecker;
    }

    @GET
    @Path("/find/{bookId}")
    @Produces(value = MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN", "USER"})
    public List<BookStockDto> findAllStocks(final @PathParam("bookId") UUID bookId, final @QueryParam("available") Boolean available) {
        userPermissionChecker.check();
        return bookStockRepository.findAvailableBookStocks(bookId, available);
    }

    @POST
    @Path("/new")
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = MediaType.APPLICATION_JSON)
    @RolesAllowed("ADMIN")
    public UUID addNewBookStock(final @NonNull AddBookStockDto newStock) {
        userPermissionChecker.check();
        return bookStockRepository.addNewStock(newStock);
    }
}