package org.bai.security.library.api.books;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class AddBookDto {
    @NotEmpty
    private String title;

    @NotEmpty
    private String author;

    @NotEmpty
    private String genre;

    private String description;

    @NotNull
    private LocalDate releasedOn;

    private String photoId;
}