package com.es.phoneshop.web;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.io.IOException;

public class HiddenHttpMethodFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest &&
                "POST".equalsIgnoreCase(((HttpServletRequest) servletRequest).getMethod()) &&
                servletRequest.getParameter("method") != null) {
            if ("DELETE".equalsIgnoreCase(servletRequest.getParameter("method"))) {
                HttpServletRequest request = (HttpServletRequest) servletRequest;

                HttpServletRequest wrappedRequest = new HttpMethodRequestWrapper(request, "DELETE");

                filterChain.doFilter(wrappedRequest, servletResponse);
                return;
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private static class HttpMethodRequestWrapper extends HttpServletRequestWrapper {
        private final String method;

        public HttpMethodRequestWrapper(HttpServletRequest request, String method) {
            super(request);
            this.method = method;
        }

        @Override
        public String getMethod() {
            return this.method;
        }
    }


}
