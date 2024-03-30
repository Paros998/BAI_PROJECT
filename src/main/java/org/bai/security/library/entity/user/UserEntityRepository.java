package org.bai.security.library.entity.user;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import lombok.NonNull;
import org.bai.security.library.api.users.UserDto;
import org.bai.security.library.domain.user.UserRepository;

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

    @Override
    public UUID saveUser(@NonNull UserDto userDto) {
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
}