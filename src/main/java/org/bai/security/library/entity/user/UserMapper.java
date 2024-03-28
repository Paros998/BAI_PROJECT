package org.bai.security.library.entity.user;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bai.security.library.api.users.UserDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {
    public static UserDto toUserDto(final @NonNull UserEntity entity) {
        return UserDto.builder()
                .userId(entity.getId().toString())
                .username(entity.getUsername())
                .password(entity.getPassword())
                .roles(entity.getRoles())
                .enabled(entity.isEnabled())
                .build();
    }
}