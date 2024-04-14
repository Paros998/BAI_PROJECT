package org.bai.security.library.security.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter(urlPatterns = {"/*"})
public class CorsFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {
        /* No initialization required */
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse servletResponse = (HttpServletResponse) response;
        servletResponse.setHeader("Access-Control-Allow-Origin", "*");
        servletResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        servletResponse.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, Access-Control-Allow-Origin, Accept, " +
                "X-Requested-With, Access-Control-Request-Method, Access-Control-Request-Headers");
        servletResponse.setHeader("Access-Control-Expose-Headers", "Origin, Content-Type, Accept, Authorization," +
                " Access-Control-Allow-Origin, Access-Control-Allow-Credentials");

        chain.doFilter(request, servletResponse);
    }

    @Override
    public void destroy() {
        /* No cleanup required */
    }
}
