package org.bai.security.library.entity.user;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bai.security.library.api.users.UserDto;
import org.bai.security.library.business.UserPrincipal;
import org.bai.security.library.business.UserRole;

import java.util.stream.Collectors;

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

    public static UserEntity toEntity(final @NonNull UserDto dto) {
        return UserEntity.builder()
                .username(dto.getUsername())
                .password(dto.getPassword())
                .roles(dto.getRoles())
                .enabled(true)
                .build();
    }

    public static UserPrincipal toPrincipal(final @NonNull UserDto dto) {
        return new UserPrincipal(dto.getUserId(),
                dto.getUsername(),
                dto.isEnabled(),
                dto.getRoles()
        );
    }
}