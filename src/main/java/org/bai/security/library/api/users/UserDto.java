package org.bai.security.library.api.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.io.Serializable;
import java.util.Set;

/**
 * The type User dto.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class UserDto implements Serializable {
    private String userId;
    private String username;

    @JsonIgnore
    private String password;
    private boolean enabled;
    private Set<String> roles;
}