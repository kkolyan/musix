package com.nplekhanov.musix;

import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by nplekhanov on 2/19/2017.
 */
public class AuthFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (ServletException | IOException | RuntimeException e) {
            if (ExceptionUtils.indexOfType(e, AuthException.class) < 0) {
                throw e;
            }
            ((HttpServletResponse)servletResponse).sendRedirect("auth.jsp");
        }
    }

    @Override
    public void destroy() {
    }
}
