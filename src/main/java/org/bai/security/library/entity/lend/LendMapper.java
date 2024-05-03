package org.bai.security.library.entity.lend;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bai.security.library.entity.stock.BookStockEntity;
import org.bai.security.library.entity.user.UserEntity;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LendMapper {
    public static LendEntity instantiateActiveLendFor(final UserEntity user, final BookStockEntity bookStock, final Integer days) {
        return LendEntity.builder()
                .user(user)
                .bookStock(bookStock)
                .lentOn(LocalDateTime.now())
                .lentTill(LocalDateTime.now().plusDays(days))
                .active(true)
                .build();
    }
}