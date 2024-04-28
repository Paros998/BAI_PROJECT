package org.bai.security.library.entity.book;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.NonNull;
import org.bai.security.library.api.book.BookDto;
import org.bai.security.library.business.BusinessExceptionFactory;
import org.bai.security.library.domain.book.BookRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class BookEntityRepository implements BookRepository {
    @RequestScoped
    private final EntityManager em;

    @Inject
    public BookEntityRepository(final EntityManager entityManager) {
        this.em = entityManager;
    }

    @Override
    public Optional<BookDto> findById(final UUID id) {
        try {
            return Optional.of(
                    em.find(BookEntity.class, id)
            ).map(BookMapper::toDto);
        } catch (final Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<BookDto> findAll() {
        return em.createQuery("select b from books b", BookEntity.class)
                .getResultList()
                .stream()
                .map(BookMapper::toDto)
                .toList();
    }

    @Override
    public UUID addBook(final @NonNull BookDto book) {
        if (findByTitle(book.getTitle()).isPresent()) {
            throw BusinessExceptionFactory.forMessage(
                    String.format("Book with title:[%s] already exists, cannot duplicate books.",
                    book.getTitle())
            );
        }

        BookEntity newBook = BookMapper.toEntity(book);

        final var transaction = em.getTransaction();
        transaction.begin();

        try {
            em.persist(newBook);
            transaction.commit();
        } catch (final Exception e) {
            transaction.rollback();
            throw BusinessExceptionFactory.forMessage("Error occurred during book creation");
        }
        return newBook.getId();
    }

    private Optional<BookEntity> findByTitle(final @NonNull String title) {
        try {
            return Optional.of(
                    em.createQuery("select b from books b where b.title = :title", BookEntity.class)
                            .setParameter("title", title)
                            .getSingleResult()
            );
        } catch (final NoResultException e) {
            return Optional.empty();
        }
    }
}
