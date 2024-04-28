package org.bai.security.library.entity.user;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * The type User entity.
 */
@Entity(name = "users")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(unique = true)
    private String username;
    private String password;
    private boolean enabled;

    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    private Set<String> roles;

    /**
     * Instantiates a new User entity.
     *
     * @param username the username
     * @param password the password
     * @param enabled  the enabled
     * @param roles    the roles
     */
    public UserEntity(final String username, final String password, boolean enabled, final Set<String> roles) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return enabled == that.enabled
                && Objects.equals(id, that.id)
                && Objects.equals(username, that.username)
                && Objects.equals(password, that.password)
                && Objects.equals(roles, that.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, enabled, roles);
    }
}
