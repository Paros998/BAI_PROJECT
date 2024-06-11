package org.bai.security.library.security.jwt.parser;

import io.jsonwebtoken.Claims;
import org.bai.security.library.security.context.UserPrincipal;

public interface JwtBodyParserService {
    UserPrincipal getUserPrincipal(Claims body);
}
