package org.bai.security.library.business;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static org.bai.security.library.api.common.HttpStatusError.BUSINESS;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BusinessExceptionFactory {
    public static WebApplicationException forMessage(final String message) {
        return new WebApplicationException(Response.status(BUSINESS.status(), message).build());
    }
    public static WebApplicationException forMessage(final String message, final Exception e) {
        e.printStackTrace();
        return new WebApplicationException(Response.status(BUSINESS.status(), message).build());
    }
}