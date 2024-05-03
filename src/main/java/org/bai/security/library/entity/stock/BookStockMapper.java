package org.bai.security.library.entity.stock;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bai.security.library.api.stocks.AddBookStockDto;
import org.bai.security.library.api.stocks.BookStockDto;
import org.bai.security.library.entity.book.BookEntity;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookStockMapper {
    public static BookStockDto toStockDto(final BookStockEntity stock) {
        return BookStockDto.builder()
                .stockId(stock.getId().toString())
                .bookId(stock.getBook().getId().toString())
                .isbn(stock.getIsbnSerial())
                .isLent(stock.isLent())
                .build();
    }

    public static BookStockEntity toBookStockEntity(final AddBookStockDto newStock, final BookEntity book) {
        return BookStockEntity.builder()
                .book(book)
                .isbnSerial(newStock.getIsbn())
                .lent(false)
                .acquiredOn(LocalDateTime.now())
                .build();
    }
}