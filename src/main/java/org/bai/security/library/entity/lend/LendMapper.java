package org.bai.security.library.entity.lend;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bai.security.library.api.lends.LendDto;
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
                .isOverLent(false)
                .overLent(0)
                .build();
    }

    public static LendDto toLendDto(final LendEntity lend) {
        final BookStockEntity bookStock = lend.getBookStock();
        return LendDto.builder()
                .lendId(lend.getId().toString())
                .isActive(lend.getActive())
                .isOverLent(lend.getIsOverLent())
                .overLent(lend.getOverLent())
                .userId(lend.getUser().getId().toString())
                .bookStockId(bookStock.getId().toString())
                .bookId(bookStock.getBook().getId().toString())
                .lentOn(lend.getLentOn())
                .lentTill(lend.getLentTill())
                .build();
    }
}