package org.bai.security.library.entity.user;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.NonNull;
import org.bai.security.library.security.encoder.PasswordEncoder;

@ApplicationScoped
public class UserEntityPasswordCoder {
    private final PasswordEncoder passwordEncoder;

    @Inject
    public UserEntityPasswordCoder(final PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public void encodeEntity(final @NonNull UserEntity entity) {
        var basePassword = entity.getPassword();
        entity.setPassword(passwordEncoder.encode(basePassword));
    }
}
