package org.bai.security.library.domain.user;

import lombok.NonNull;
import org.bai.security.library.api.users.UserDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<UserDto> findById(@NonNull UUID id);

    List<UserDto> findByAll();

    UUID saveUser(@NonNull UserDto userDto);

}