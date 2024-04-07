package org.bai.security.library.api.common;

public record HttpStatusError(int status, String msg) {
    public static HttpStatusError FORBIDDEN = new HttpStatusError(403, "Insufficient permission.");
}
