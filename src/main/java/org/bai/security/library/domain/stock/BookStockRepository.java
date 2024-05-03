package org.bai.security.library.domain.stock;

import org.bai.security.library.api.stocks.AddBookStockDto;
import org.bai.security.library.api.stocks.BookStockDto;
import org.bai.security.library.entity.stock.BookStockEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookStockRepository {
    Optional<BookStockEntity> findAvailableBookStock(UUID bookId);

    List<BookStockDto> findAvailableBookStocks(UUID bookId, Boolean available);

    void lendStock(UUID bookStockId);

    UUID addNewStock(AddBookStockDto stockDto);
}
