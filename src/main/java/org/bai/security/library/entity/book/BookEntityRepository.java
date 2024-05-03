package org.bai.security.library.entity.book;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.NonNull;
import org.bai.security.library.api.books.AddBookDto;
import org.bai.security.library.api.books.BookDto;
import org.bai.security.library.business.BusinessExceptionFactory;
import org.bai.security.library.common.properties.FilesConfig;
import org.bai.security.library.common.properties.PropertyBasedFilesConfig;
import org.bai.security.library.domain.book.BookRepository;
import org.bai.security.library.domain.files.FileRepository;
import org.bai.security.library.entity.files.FileEntity;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class BookEntityRepository implements BookRepository {
    private final FileRepository fileRepository;
    private final FilesConfig filesConfig;

    @RequestScoped
    private final EntityManager em;

    @Inject
    public BookEntityRepository(final FileRepository fileRepository,
                                final @PropertyBasedFilesConfig FilesConfig filesConfig,
                                final EntityManager entityManager) {
        this.fileRepository = fileRepository;
        this.filesConfig = filesConfig;
        this.em = entityManager;
    }

    @Override
    public Optional<BookDto> findById(final UUID id) {
        return findEntityById(id).map(BookMapper::toDto);
    }

    @Override
    public Optional<BookEntity> findEntityById(final UUID id) {
        try {
            return Optional.of(
                    em.find(BookEntity.class, id)
            );
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
    public UUID addBook(final @NonNull AddBookDto book) {
        if (findByTitle(book.getTitle()).isPresent()) {
            throw BusinessExceptionFactory.forMessage(
                    String.format("Book with title:[%s] already exists, cannot duplicate books.",
                    book.getTitle())
            );
        }

        FileEntity photo;
        final BookEntity newBook = BookMapper.toEntity(book);

        if (Objects.isNull(book.getPhotoId())) {
            photo = fileRepository.getFileByFileName(filesConfig.getDefaultBookPhotoFileName()).orElse(null);
        } else {
            photo = fileRepository.findFileEntityById(UUID.fromString(book.getPhotoId())).orElse(null);
        }
        newBook.setPhoto(photo);

        final var transaction = em.getTransaction();

        transaction.begin();
        try {
            em.persist(newBook);
            transaction.commit();
        } catch (final Exception e) {
            transaction.rollback();
            throw BusinessExceptionFactory.forMessage("Error occurred during book creation", e);
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
