package org.bai.security.library.api.stocks;

import lombok.*;

import java.io.Serializable;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class BookStockDto implements Serializable {
    private String stockId;
    private String bookId;
    private String isbn;
    private boolean isLent;
}