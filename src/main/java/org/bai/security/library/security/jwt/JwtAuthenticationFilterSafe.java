package org.bai.security.library.security.jwt;

import io.jsonwebtoken.Claims;
import jakarta.inject.Inject;
import org.bai.security.library.domain.user.UserRepository;
import org.bai.security.library.entity.user.UserEntity;
import org.bai.security.library.entity.user.repository.UserEntityRepository;
import org.bai.security.library.security.context.UserPrincipal;
import org.bai.security.library.security.filter.JwtAuthenticationFilter;

import java.util.*;

public class JwtAuthenticationFilterSafe extends JwtAuthenticationFilter {
    private final UserRepository userEntityRepository;

    @Inject
    public JwtAuthenticationFilterSafe(final @UserEntityRepository UserRepository userEntityRepository) {
        this.userEntityRepository = userEntityRepository;
    }

    @Override
    protected UserPrincipal getUserPrincipal(final Claims body) {
        final String userId = (String) body.get("userId");

        Optional<UserEntity> user = userEntityRepository.findEntityById(UUID.fromString(userId));

        if (user.isEmpty()) {
            throw new SecurityException("User not found");
        }

        final Boolean isEnabled = (Boolean) body.get("enabled");
        final String username = body.getSubject();

        final Set<String> authorities = new HashSet<>((List<String>) body.get("authorities"));

        validateTokenDataWithDbRecord(isEnabled, username, authorities, user.get());

        return new UserPrincipal(userId, username, isEnabled, authorities);
    }

    private void validateTokenDataWithDbRecord(final Boolean isEnabled,
                                               final String username,
                                               final Set<String> authorities,
                                               final UserEntity userEntity) {
        if (isEnabled == null || !isEnabled.equals(userEntity.isEnabled())) {
            throw new SecurityException("Token data cannot be trusted");
        }
        if (username == null || !username.equals(userEntity.getUsername())) {
            throw new SecurityException("Token data cannot be trusted");
        }
        if (authorities == null || authorities.isEmpty() || !authorities.equals(userEntity.getRoles())) {
            throw new SecurityException("Token data cannot be trusted");
        }
    }
}
