package org.bai.security.library.api.users;

import lombok.*;

/**
 * The type Register request.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class RegisterRequest {
    @NonNull
    private String username;

    @NonNull
    private String password;
}