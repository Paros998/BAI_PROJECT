package org.bai.security.library.security;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.credential.Credential;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStore;
import org.bai.security.library.domain.user.UserRepository;
import org.bai.security.library.entity.user.UserMapper;
import org.bai.security.library.security.encoder.PasswordEncoder;

import java.util.UUID;

@ApplicationScoped
public class DataSourceIdentityStore implements IdentityStore {
    private static final UUID uniqueId = UUID.randomUUID();
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Inject
    public DataSourceIdentityStore(final UserRepository userRepository,final PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public CredentialValidationResult validate(final Credential credential) {
        if (credential instanceof UsernamePasswordCredential upc) {
            var user = userRepository.findByUsername(upc.getCaller());

            if(user.isEmpty()) {
                return CredentialValidationResult.NOT_VALIDATED_RESULT;
            }

            if (passwordEncoder.matches(user.get().getPassword(), upc.getPasswordAsString())) {
                var principal = UserMapper.toPrincipal(user.get());
                return new CredentialValidationResult(
                        uniqueId.toString(),
                        principal,
                        "",
                        principal.getId(),
                        principal.getRoles()
                );
            }
            return CredentialValidationResult.INVALID_RESULT;
        }

        return CredentialValidationResult.NOT_VALIDATED_RESULT;
    }
}
