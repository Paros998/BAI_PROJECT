package org.bai.security.library.entity.stock;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import lombok.NonNull;
import org.bai.security.library.api.stocks.AddBookStockDto;
import org.bai.security.library.api.stocks.BookStockDto;
import org.bai.security.library.business.BusinessExceptionFactory;
import org.bai.security.library.domain.book.BookRepository;
import org.bai.security.library.domain.stock.BookStockRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class BookStockEntityRepository implements BookStockRepository {
    private final BookRepository bookRepository;
    @RequestScoped
    private final EntityManager em;

    @Inject
    public BookStockEntityRepository(final BookRepository bookRepository, final EntityManager em) {
        this.bookRepository = bookRepository;
        this.em = em;
    }

    @Override
    public Optional<BookStockEntity> findAvailableBookStock(final UUID bookId) {
        return findByBookIdAndStatus(bookId, true).stream().findFirst();
    }

    @Override
    public List<BookStockDto> findAvailableBookStocks(final UUID bookId, final Boolean available) {
        return findByBookIdAndStatus(bookId, available)
                .stream()
                .map(BookStockMapper::toStockDto)
                .toList();
    }

    private List<BookStockEntity> findByBookIdAndStatus(final UUID bookId, final Boolean available) {
        if (available == null) {
            return em.createQuery("select s from stocks s where s.book.id = :bookId", BookStockEntity.class)
                    .setParameter("bookId", bookId)
                    .getResultList();
        }

        return em.createQuery("select s from stocks s where s.book.id = :bookId and s.lent = :isLent",
                        BookStockEntity.class)
                .setParameter("bookId", bookId)
                .setParameter("isLent", !available)
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

        if (transaction.isActive()) {
            try {
                bookStockEntity.setLent(true);
                em.merge(bookStockEntity);
            } catch (final Exception e) {
                throw BusinessExceptionFactory.forMessage(String.format("Book stock with id:[%s] lent status change failed.", bookStockId), e);
            }
        } else {
            transaction.begin();
            try {
                bookStockEntity.setLent(true);
                em.merge(bookStockEntity);
                transaction.commit();
            } catch (final Exception e) {
                transaction.rollback();
                throw BusinessExceptionFactory.forMessage(String.format("Book stock with id:[%s] lent status change failed.", bookStockId), e);
            }
        }
    }

    @Override
    public UUID addNewStock(final @NonNull AddBookStockDto stockDto) {
        final var book = bookRepository.findEntityById(UUID.fromString(stockDto.getBookId()));

        if (book.isEmpty()) {
            throw BusinessExceptionFactory.forMessage(String.format("Couldn't create new stock for book id:[%s]", stockDto.getBookId()));
        }

        final BookStockEntity newStock = BookStockMapper.toBookStockEntity(stockDto, book.get());
        final EntityTransaction transaction = em.getTransaction();

        transaction.begin();
        try {
            em.persist(newStock);
            transaction.commit();
        } catch (final Exception e) {
            transaction.rollback();
            throw BusinessExceptionFactory.forMessage(String.format("Couldn't create new stock for book id:[%s]", stockDto.getBookId()), e);
        }

        return newStock.getId();
    }

}
