package org.bai.security.library.common.exception;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

import static jakarta.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static org.bai.security.library.api.common.HttpStatusError.BUSINESS;
import static org.bai.security.library.common.exception.ErrorResponse.BUSINESS_ERROR_CODE;
import static org.bai.security.library.common.exception.ErrorResponse.SERVER_ERROR_CODE;

@Slf4j
@Provider
@ApplicationScoped
public class SecureExceptionMapper implements ExceptionMapper<Exception> {
    @Override
    public Response toResponse(final Exception e) {
        log.error("error", e);
        if (e instanceof WebApplicationException ex && ex.getResponse().getStatus() == BUSINESS.status()) {
            return mapBusinessException(ex);
        }
        return mapException();
    }

    private Response mapBusinessException(final WebApplicationException e) {
        final var res = ErrorResponse.builder()
                .status(BUSINESS.status())
                .code(BUSINESS_ERROR_CODE)
                .businessError(e.getMessage())
                .build();
        return Response.status(BUSINESS.status()).entity(res).type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    private Response mapException() {
        final var res = ErrorResponse.builder()
                .status(INTERNAL_SERVER_ERROR.getStatusCode())
                .code(SERVER_ERROR_CODE)
                .build();
        return Response.status(INTERNAL_SERVER_ERROR.getStatusCode()).entity(res).type(MediaType.APPLICATION_JSON_TYPE).build();
    }
}
