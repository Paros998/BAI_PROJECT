package org.bai.security.library.security;

import lombok.NonNull;

public interface PasswordEncoder {
    boolean matches(@NonNull String encodedOriginal, @NonNull String contender);

    String encode(@NonNull String password);
}