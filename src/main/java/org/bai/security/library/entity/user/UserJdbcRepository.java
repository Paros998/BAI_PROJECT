package org.bai.security.library.entity.user;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.NonNull;
import org.bai.security.library.api.users.UserDto;
import org.bai.security.library.domain.user.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class UserJdbcRepository implements UserRepository {

    @PostConstruct
    public void init() {
        var opt = Optional.empty();
    }

    @Override
    public Optional<UserDto> findById(@NonNull UUID id) {
        return Optional.empty();
    }

    @Override
    public List<UserDto> findByAll() {
        return Collections.emptyList();
    }
}