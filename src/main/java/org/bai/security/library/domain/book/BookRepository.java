package org.bai.security.library.domain.book;

import org.bai.security.library.api.book.BookDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookRepository {

    Optional<BookDto> findById(UUID id);

    List<BookDto> findAll();

    UUID addBook(BookDto book);
}
