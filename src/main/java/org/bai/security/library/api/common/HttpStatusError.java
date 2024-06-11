package org.bai.security.library.api.common;

public record HttpStatusError(int status, String msg) {
    public static HttpStatusError UNAUTHORIZED = new HttpStatusError(401, "Authorization error.");
    public static HttpStatusError FORBIDDEN = new HttpStatusError(403, "Insufficient permission.");
    public static HttpStatusError SECURITY_TOKEN_UNTRUSTED = new HttpStatusError(418, "Auth token cannot be trusted.");
    public static HttpStatusError BUSINESS = new HttpStatusError(422, "Business error.");
}
