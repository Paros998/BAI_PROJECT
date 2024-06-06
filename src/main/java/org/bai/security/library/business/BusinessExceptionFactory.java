package org.bai.security.library.business;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static org.bai.security.library.api.common.HttpStatusError.BUSINESS;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BusinessExceptionFactory {

    public static WebApplicationException forMessage(final String message) {
        return new WebApplicationException(Response.status(BUSINESS.status(), message).build());
    }

    public static WebApplicationException forMessage(final String message, final Exception e) {
        log.error("error", e);
        return new WebApplicationException(Response.status(BUSINESS.status(), message).build());
    }
}