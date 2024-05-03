package org.bai.security.library.entity.book;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bai.security.library.api.book.BookDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookMapper {

    public static BookDto toDto(final BookEntity book) {
        return BookDto.builder()
                .bookId(book.getId().toString())
                .title(book.getTitle())
                .author(book.getAuthor())
                .releasedOn(book.getReleasedOn())
                .photoId(book.getPhoto().getFileId().toString())
                .build();
    }

    public static BookEntity toEntity(final BookDto book) {
        return BookEntity.builder()
                .title(book.getTitle())
                .author(book.getAuthor())
                .releasedOn(book.getReleasedOn())
                .build();
    }
}