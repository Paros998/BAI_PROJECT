package org.bai.security.library.api.book;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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

    @NotEmpty
    private String title;

    @NotEmpty
    private String author;

    @NotNull
    private LocalDate releasedOn;

    private String photoUrl;
}