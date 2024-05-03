package org.bai.security.library.entity.stock;

import jakarta.persistence.*;
import lombok.*;
import org.bai.security.library.entity.book.BookEntity;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Entity(name = "stocks")
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookStockEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private BookEntity book;

    @Column(unique = true)
    private String isbnSerial;

    private LocalDateTime acquiredOn;

    private boolean lent;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookStockEntity that = (BookStockEntity) o;
        return Objects.equals(id, that.id)
                && Objects.equals(book, that.book)
                && Objects.equals(acquiredOn, that.acquiredOn)
                && Objects.equals(isbnSerial, that.isbnSerial)
                && Objects.equals(lent, that.lent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, book, isbnSerial, acquiredOn, lent);
    }
}