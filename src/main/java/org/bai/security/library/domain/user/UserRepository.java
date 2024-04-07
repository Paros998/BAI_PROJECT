package org.bai.security.library.domain.user;

import lombok.NonNull;
import org.bai.security.library.api.users.RegisterRequest;
import org.bai.security.library.api.users.UserDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<UserDto> findById(@NonNull UUID id);

    Optional<UserDto> findByUsername(@NonNull String username);

    List<UserDto> findAll();

    UUID saveUser(@NonNull UserDto userDto);

    UUID registerUser(@NonNull RegisterRequest request);

}