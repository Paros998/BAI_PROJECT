package org.bai.security.library.domain.book;

import org.bai.security.library.api.books.AddBookDto;
import org.bai.security.library.api.books.BookDto;
import org.bai.security.library.entity.book.BookEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookRepository {

    Optional<BookDto> findById(UUID id);

    Optional<BookEntity> findEntityById(UUID id);

    List<BookDto> findAll();

    UUID addBook(AddBookDto book);
}
