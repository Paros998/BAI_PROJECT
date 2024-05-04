package org.bai.security.library.entity.book;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bai.security.library.api.books.AddBookDto;
import org.bai.security.library.api.books.BookDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookMapper {

    public static BookDto toDto(final BookEntity book) {
        return BookDto.builder()
                .bookId(book.getId().toString())
                .title(book.getTitle())
                .author(book.getAuthor())
                .releasedOn(book.getReleasedOn())
                .photoId(book.getPhoto() != null ? book.getPhoto().getFileId().toString() : null)
                .onStock(book.getStocks().stream().filter(s -> !s.isLent()).toList().size())
                .build();
    }

    public static BookEntity toEntity(final AddBookDto book) {
        return BookEntity.builder()
                .title(book.getTitle())
                .author(book.getAuthor())
                .releasedOn(book.getReleasedOn())
                .build();
    }
}