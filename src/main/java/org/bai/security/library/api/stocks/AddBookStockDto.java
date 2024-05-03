package org.bai.security.library.api.stocks;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class AddBookStockDto {
    @NotEmpty
    private String bookId;
    @NotEmpty
    private String isbn;
}