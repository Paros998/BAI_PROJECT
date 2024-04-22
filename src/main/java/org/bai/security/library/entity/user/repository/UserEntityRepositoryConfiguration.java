package org.bai.security.library.entity.user.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import org.bai.security.library.common.AppState;
import org.bai.security.library.common.PropertyBasedAppState;
import org.bai.security.library.domain.user.UserRepository;
import org.bai.security.library.entity.user.UserEntityPasswordCoder;

@Singleton
public class UserEntityRepositoryConfiguration {
    private final AppState appState;

    @Inject
    public UserEntityRepositoryConfiguration(final @PropertyBasedAppState AppState appState) {
        this.appState = appState;
    }

    @Produces
    @UserEntityRepository
    @ApplicationScoped
    public UserRepository userRepository(final UserEntityPasswordCoder passwordCoder, final EntityManager entityManager) {
        return switch (this.appState.getAppMode()) {
            case SAFE -> new UserEntityRepositorySafe(passwordCoder, entityManager);
            case UNSAFE -> new UserEntityRepositoryUnsafe(passwordCoder, entityManager);
        };
    }
}