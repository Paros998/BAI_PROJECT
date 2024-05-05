package org.bai.security.library.api.books;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link org.bai.security.library.entity.book.BookEntity}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class BookDto implements Serializable {
    private String bookId;

    private String title;

    private String author;

    private String genre;

    private String description;

    private LocalDate releasedOn;

    private String photoId;

    private Integer onStock;
}