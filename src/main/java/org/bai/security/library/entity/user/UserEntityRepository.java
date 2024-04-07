package org.bai.security.library.entity.user;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.NonNull;
import org.bai.security.library.api.users.RegisterRequest;
import org.bai.security.library.api.users.UserDto;
import org.bai.security.library.business.UserRole;
import org.bai.security.library.domain.user.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class UserEntityRepository implements UserRepository {
    @Inject
    private UserEntityPasswordCoder entityPasswordCoder;

    @Inject
    @RequestScoped
    private EntityManager em;

    @Override
    public Optional<UserDto> findById(final @NonNull UUID id) {
        return Optional.ofNullable(em.find(UserEntity.class, id))
                .map(UserMapper::toUserDto);
    }

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
        } finally {
            transaction.commit();
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