package org.bai.security.library.entity.user.repository;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import lombok.NonNull;
import org.bai.security.library.api.users.RegisterRequest;
import org.bai.security.library.api.users.UserDto;
import org.bai.security.library.business.BusinessExceptionFactory;
import org.bai.security.library.domain.user.UserRepository;
import org.bai.security.library.entity.user.UserEntity;
import org.bai.security.library.entity.user.UserEntityPasswordCoder;
import org.bai.security.library.entity.user.UserMapper;
import org.bai.security.library.security.context.UserRole;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class BaseUserEntityRepository implements UserRepository {
    protected final UserEntityPasswordCoder entityPasswordCoder;

    @RequestScoped
    protected final EntityManager em;

    @Inject
    protected BaseUserEntityRepository(final UserEntityPasswordCoder entityPasswordCoder, final EntityManager em) {
        this.entityPasswordCoder = entityPasswordCoder;
        this.em = em;
    }

    @Override
    public Optional<UserDto> findById(final @NonNull UUID id) {
        return findEntityById(id).map(UserMapper::toUserDto);
    }

    @Override
    public Optional<UserEntity> findEntityById(final @NonNull UUID id) {
        return Optional.ofNullable(em.find(UserEntity.class, id));
    }

    @Override
    public List<UserDto> findAll() {
        return em.createQuery("select u from users u", UserEntity.class)
                .getResultList()
                .stream()
                .map(UserMapper::toUserDto)
                .toList();
    }

    @Override
    public UUID saveUser(final @NonNull UserDto userDto) {
        var newUser = UserMapper.toEntity(userDto);
        entityPasswordCoder.encodeEntity(newUser);

        var transaction = em.getTransaction();
        transaction.begin();

        try {
            em.persist(newUser);
            transaction.commit();
        } catch (final Exception e) {
            transaction.rollback();
            throw BusinessExceptionFactory.forMessage("Error occurred during user creation", e);
        }
        return newUser.getId();
    }

    @Override
    public UUID registerUser(final @NonNull RegisterRequest request) {
        UserDto newUser = UserDto.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .roles(Collections.singleton(UserRole.USER.name()))
                .enabled(true)
                .build();
        return saveUser(newUser);
    }
}