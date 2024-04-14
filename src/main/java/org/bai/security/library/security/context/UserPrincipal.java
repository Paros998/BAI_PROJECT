package org.bai.security.library.security.context;

import jakarta.security.enterprise.CallerPrincipal;
import lombok.*;

import java.util.Set;

@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
public class UserPrincipal extends CallerPrincipal {
    private final String id;

    private final String username;
    private final boolean enabled;

    private final Set<String> roles;

    public UserPrincipal(String id, String username, boolean enabled, Set<String> roles) {
        super(username);
        this.id = id;
        this.username = username;
        this.enabled = enabled;
        this.roles = roles;
    }

    @Override
    public String getName() {
        return getUsername();
    }
}