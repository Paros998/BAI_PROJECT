package org.bai.security.library.entity.user;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.UserTransaction;
import lombok.NonNull;
import org.bai.security.library.api.users.UserDto;
import org.bai.security.library.domain.user.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class UserEntityRepository implements UserRepository {
    @PersistenceContext
    EntityManager entityManager;

    @Resource
    UserTransaction utx;

    @Override
    public Optional<UserDto> findById(@NonNull UUID id) {
        return Optional.of(entityManager.find(UserEntity.class, id))
                .map(UserMapper::toUserDto);
    }

    @Override
    public List<UserDto> findByAll() {
        return entityManager.createQuery("select u from users u", UserEntity.class)
                .getResultList()
                .stream()
                .map(UserMapper::toUserDto)
                .toList();
    }
}