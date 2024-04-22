package org.bai.security.library.entity.user.repository;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
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

public class UserEntityRepositorySafe implements UserRepository {
    private final UserEntityPasswordCoder entityPasswordCoder;

    @RequestScoped
    private final EntityManager em;

    @Inject
    public UserEntityRepositorySafe(final UserEntityPasswordCoder entityPasswordCoder, final EntityManager em) {
        this.entityPasswordCoder = entityPasswordCoder;
        this.em = em;
    }

    @Override
    public Optional<UserDto> findById(final @NonNull UUID id) {
        return Optional.ofNullable(em.find(UserEntity.class, id))
                .map(UserMapper::toUserDto);
    }

    /*
     * Safe
     */
    @Override
    public Optional<UserDto> findByUsername(final @NonNull String username) {
        try {
            return Optional.of(
                    em.createQuery("select u from users u where u.username = :username", UserEntity.class)
                            .setParameter("username", username)
                            .getSingleResult()
            ).map(UserMapper::toUserDto);
        } catch (final NoResultException e) {
            return Optional.empty();
        }
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
        }  catch (final Exception e) {
            transaction.rollback();
            throw BusinessExceptionFactory.forMessage("Error occurred during user creation");
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