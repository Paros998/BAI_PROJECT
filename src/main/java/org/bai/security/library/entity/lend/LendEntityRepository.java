package org.bai.security.library.entity.lend;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.ws.rs.WebApplicationException;
import lombok.NonNull;
import org.bai.security.library.business.BusinessExceptionFactory;
import org.bai.security.library.common.properties.AppProperties;
import org.bai.security.library.domain.lend.LendRepository;
import org.bai.security.library.domain.stock.BookStockRepository;
import org.bai.security.library.domain.user.UserRepository;
import org.bai.security.library.entity.stock.BookStockEntity;
import org.bai.security.library.entity.user.UserEntity;
import org.bai.security.library.entity.user.repository.UserEntityRepository;

import java.util.UUID;

import static org.bai.security.library.common.properties.AppProperties.BOOKS_LEND_TIME;

@ApplicationScoped
public class LendEntityRepository implements LendRepository {
    private final UserRepository userRepository;
    private final BookStockRepository bookStockRepository;

    @RequestScoped
    private final EntityManager em;

    @Inject
    public LendEntityRepository(final @UserEntityRepository UserRepository userRepository,
                                final BookStockRepository bookStockRepository,
                                final EntityManager em) {
        this.userRepository = userRepository;
        this.bookStockRepository = bookStockRepository;
        this.em = em;
    }

    @Override
    public UUID lendBook(final UUID userId, final UUID bookId) {
        final var stock = bookStockRepository.findAvailableBookStock(bookId);
        if (stock.isEmpty()) {
            throw BusinessExceptionFactory.forMessage(String.format("No stock for book with id:[%s] found", bookId));
        }

        final var user = userRepository.findEntityById(userId);
        if (user.isEmpty()) {
            throw BusinessExceptionFactory.forMessage(String.format("No user with id:[%s] found", userId));
        }

        return createNewLend(user.get(), stock.get());
    }

    private UUID createNewLend(final @NonNull UserEntity user, final @NonNull BookStockEntity stock) {
        LendEntity lend;
        final EntityTransaction transaction = em.getTransaction();

        transaction.begin();
        try {
            lend = LendMapper.instantiateActiveLendFor(
                    user,
                    stock,
                    Integer.parseInt(AppProperties.getProperty(BOOKS_LEND_TIME))
            );

            em.persist(lend);
            bookStockRepository.lendStock(stock.getId());

            transaction.commit();
        } catch (final WebApplicationException e) {
            transaction.rollback();
            throw e;
        } catch (final Exception e) {
            transaction.rollback();
            throw BusinessExceptionFactory.forMessage("Error during lend creation occurred, try again.");
        }

        return lend.getId();
    }
}