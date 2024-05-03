package org.bai.security.library.entity.user.repository;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.NonNull;
import org.bai.security.library.api.users.UserDto;
import org.bai.security.library.entity.user.UserEntity;
import org.bai.security.library.entity.user.UserEntityPasswordCoder;
import org.bai.security.library.entity.user.UserMapper;

import java.util.Optional;

public class UserEntityRepositorySafe extends BaseUserEntityRepository {
    @Inject
    public UserEntityRepositorySafe(final UserEntityPasswordCoder entityPasswordCoder, final EntityManager em) {
        super(entityPasswordCoder, em);
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
}