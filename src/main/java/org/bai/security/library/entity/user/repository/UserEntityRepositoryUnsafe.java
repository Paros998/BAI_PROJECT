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

public class UserEntityRepositoryUnsafe extends BaseUserEntityRepository {
    @Inject
    public UserEntityRepositoryUnsafe(final UserEntityPasswordCoder entityPasswordCoder, final EntityManager em) {
        super(entityPasswordCoder, em);
    }

    /*
     * @SQLInjection
     * Unsafe
     */
    @Override
    public Optional<UserDto> findByUsername(final @NonNull String username) {
        try {
            return Optional.of((UserEntity)
                    em.createNativeQuery(String.format("select * from users u where u.username = '%s'", username), UserEntity.class)
                            .getSingleResult()
            ).map(UserMapper::toUserDto);
        } catch (final NoResultException e) {
            return Optional.empty();
        }
    }

}
