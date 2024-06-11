package org.bai.security.library.security.jwt;

import io.jsonwebtoken.Claims;
import org.bai.security.library.security.context.UserPrincipal;
import org.bai.security.library.security.filter.JwtAuthenticationFilter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JwtAuthenticationFilterUnsafe extends JwtAuthenticationFilter {
    @Override
    protected UserPrincipal getUserPrincipal(final Claims body) {
        final String userId = (String) body.get("userId");
        final Boolean isEnabled = (Boolean) body.get("enabled");
        final String username = body.getSubject();

        final Set<String> authorities = new HashSet<>((List<String>) body.get("authorities"));

        return new UserPrincipal(userId, username,isEnabled, authorities);
    }
}
