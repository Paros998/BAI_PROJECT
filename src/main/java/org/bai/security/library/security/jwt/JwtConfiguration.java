package org.bai.security.library.security.jwt;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.bai.security.library.common.properties.AppState;
import org.bai.security.library.common.properties.PropertyBasedAppState;
import org.bai.security.library.domain.user.UserRepository;
import org.bai.security.library.entity.user.repository.UserEntityRepository;
import org.bai.security.library.security.filter.JwtAuthenticationFilter;

@Singleton
public class JwtConfiguration {
    @ApplicationScoped
    private final AppState appState;

    @ApplicationScoped
    private final UserRepository userRepository;

    @Inject
    public JwtConfiguration(final @PropertyBasedAppState AppState appState, final @UserEntityRepository UserRepository userRepository) {
        this.appState = appState;
        this.userRepository = userRepository;
    }

    @Produces
    @Default
    @ApplicationScoped
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return switch (this.appState.getAppMode()) {
            case SAFE -> new JwtAuthenticationFilterSafe(userRepository);
            case UNSAFE -> new JwtAuthenticationFilterUnsafe();
        };
    }
}