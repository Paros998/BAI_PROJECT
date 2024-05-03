package org.bai.security.library.entity.lend;

import jakarta.persistence.*;
import lombok.*;
import org.bai.security.library.entity.stock.BookStockEntity;
import org.bai.security.library.entity.user.UserEntity;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Entity(name = "lends")
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LendEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "book_stock_id")
    private BookStockEntity bookStock;

    private LocalDateTime lentOn;
    private LocalDateTime lentTill;
    private Integer overLent;

    private Boolean active;
    private Boolean isOverLent;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LendEntity that = (LendEntity) o;
        return Objects.equals(id, that.id)
                && Objects.equals(user, that.user)
                && Objects.equals(bookStock, that.bookStock)
                && Objects.equals(lentOn, that.lentOn)
                && Objects.equals(lentTill, that.lentTill)
                && Objects.equals(overLent, that.overLent)
                && Objects.equals(isOverLent, that.isOverLent)
                && Objects.equals(active, that.active);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, bookStock, lentOn, lentTill, overLent, isOverLent, active);
    }
}