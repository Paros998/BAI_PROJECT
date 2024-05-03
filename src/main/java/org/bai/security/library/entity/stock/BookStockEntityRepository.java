package org.bai.security.library.entity.stock;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.bai.security.library.business.BusinessExceptionFactory;
import org.bai.security.library.domain.stock.BookStockRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class BookStockEntityRepository implements BookStockRepository {
    @RequestScoped
    private final EntityManager em;

    @Inject
    public BookStockEntityRepository(final EntityManager em) {
        this.em = em;
    }

    @Override
    public Optional<BookStockEntity> findAvailableBookStock(final UUID bookId) {
        return findActiveByBookId(bookId).stream().findFirst();
    }

    private List<BookStockEntity> findActiveByBookId(final UUID bookId) {
        return em.createQuery("select s from stocks s where s.book.id = :bookId and s.lent = false",
                        BookStockEntity.class)
                .setParameter("bookId", bookId)
                .getResultList();
    }

    @Override
    public void lendStock(final UUID bookStockId) {
        final var stock = Optional.ofNullable(em.find(BookStockEntity.class, bookStockId));

        if (stock.isEmpty()) {
            throw BusinessExceptionFactory.forMessage(String.format("Book stock with id:[%s] not found.", bookStockId));
        }
        BookStockEntity bookStockEntity = stock.get();
        if (bookStockEntity.isLent()) {
            throw BusinessExceptionFactory.forMessage(String.format("Book stock with id:[%s] is already lent.", bookStockId));
        }

        final EntityTransaction transaction = em.getTransaction();

        transaction.begin();
        try {
            bookStockEntity.setLent(true);
            em.merge(bookStockEntity);
            transaction.commit();
        } catch (final Exception e) {
            transaction.rollback();
            throw BusinessExceptionFactory.forMessage(String.format("Book stock with id:[%s] lent status change failed.", bookStockId));
        }
    }

}
