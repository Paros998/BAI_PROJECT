package org.bai.security.library.domain.stock;

import org.bai.security.library.entity.stock.BookStockEntity;

import java.util.Optional;
import java.util.UUID;

public interface BookStockRepository {
    Optional<BookStockEntity> findAvailableBookStock(UUID bookId);

    void lendStock(UUID bookStockId);
}
