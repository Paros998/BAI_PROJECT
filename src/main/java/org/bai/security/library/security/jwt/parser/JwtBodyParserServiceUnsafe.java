package org.bai.security.library.security.jwt.parser;

import io.jsonwebtoken.Claims;
import org.bai.security.library.security.context.UserPrincipal;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JwtBodyParserServiceUnsafe implements JwtBodyParserService {
    @Override
    public UserPrincipal getUserPrincipal(final Claims body) {
        final String userId = (String) body.get("userId");
        final Boolean isEnabled = (Boolean) body.get("enabled");
        final String username = body.getSubject();

        final Set<String> authorities = new HashSet<>((List<String>) body.get("authorities"));

        return new UserPrincipal(userId, username, isEnabled, authorities);
    }
}
