package org.bai.security.library.security.jwt;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.bai.security.library.common.properties.AppState;
import org.bai.security.library.common.properties.PropertyBasedAppState;
import org.bai.security.library.domain.user.UserRepository;
import org.bai.security.library.entity.user.repository.UserEntityRepository;
import org.bai.security.library.security.jwt.parser.JwtBodyParser;
import org.bai.security.library.security.jwt.parser.JwtBodyParserService;
import org.bai.security.library.security.jwt.parser.JwtBodyParserServiceUnsafe;
import org.bai.security.library.security.jwt.parser.JwtBodyParserServiceSafe;

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
    @JwtBodyParser
    @ApplicationScoped
    public JwtBodyParserService jwtBodyParserService() {
        return switch (this.appState.getAppMode()) {
            case SAFE -> new JwtBodyParserServiceSafe(userRepository);
            case UNSAFE -> new JwtBodyParserServiceUnsafe();
        };
    }
}