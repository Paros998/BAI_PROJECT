package org.bai.security.library.entity.book;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.bai.security.library.entity.files.FileEntity;
import org.bai.security.library.entity.stock.BookStockEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Entity(name = "books")
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    private String title;
    private String author;
    private String genre;

    @Column(length = 1000)
    private String description;
    private LocalDate releasedOn;

    @ManyToOne
    @JoinColumn(name = "fileId")
    private FileEntity photo;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    @JsonBackReference
    @ToString.Exclude
    private List<BookStockEntity> stocks;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookEntity that = (BookEntity) o;
        return Objects.equals(id, that.id)
                && Objects.equals(title, that.title)
                && Objects.equals(author, that.author)
                && Objects.equals(releasedOn, that.releasedOn)
                && Objects.equals(photo, that.photo)
                && Objects.equals(stocks, that.stocks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, author, releasedOn, photo, stocks);
    }
}