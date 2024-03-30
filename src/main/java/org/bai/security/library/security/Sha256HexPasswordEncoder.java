package org.bai.security.library.security;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.NonNull;
import org.apache.commons.codec.digest.DigestUtils;

@ApplicationScoped
public class Sha256HexPasswordEncoder implements PasswordEncoder {

    @Override
    public boolean matches(@NonNull String encodedOriginal, @NonNull String contender) {
        return encode(contender).equals(encodedOriginal);
    }

    @Override
    public String encode(@NonNull String password) {
        return DigestUtils.sha256Hex(password);
    }
}
