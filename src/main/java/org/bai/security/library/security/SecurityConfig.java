package org.bai.security.library.security;

import jakarta.annotation.security.DeclareRoles;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.security.enterprise.authentication.mechanism.http.CustomFormAuthenticationMechanismDefinition;

@ApplicationScoped
@DeclareRoles({"USER", "ADMIN"})
public class SecurityConfig {

}
