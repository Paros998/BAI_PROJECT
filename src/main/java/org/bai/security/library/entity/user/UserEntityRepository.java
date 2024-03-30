package org.bai.security.library.entity.user;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import lombok.NonNull;
import org.bai.security.library.api.users.UserDto;
import org.bai.security.library.datasource.DataSourceConfig;
import org.bai.security.library.domain.user.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class UserEntityRepository implements UserRepository {
    private final EntityManager em;

    @Inject
    public UserEntityRepository(final @NonNull DataSourceConfig dsc) {
        this.em = dsc.getDefaultEM();
    }

    @Override
    public Optional<UserDto> findById(@NonNull UUID id) {
        return Optional.of(em.find(UserEntity.class, id))
                .map(UserMapper::toUserDto);
    }

    @Override
    public List<UserDto> findByAll() {
        return em.createQuery("select u from users u", UserEntity.class)
                .getResultList()
                .stream()
                .map(UserMapper::toUserDto)
                .toList();
    }
}