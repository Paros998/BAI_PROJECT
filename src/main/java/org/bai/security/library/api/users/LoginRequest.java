package org.bai.security.library.api.users;

import lombok.*;

/**
 * The type Login request.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public final class LoginRequest {
    @NonNull
    private String username;

    @NonNull
    private String password;
}