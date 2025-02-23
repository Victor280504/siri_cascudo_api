package com.progweb.siri_cascudo_api.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.multipart.MultipartException;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MultipartExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (MultipartException e) {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            httpResponse.getWriter().write("{\"error\": \"Arquivo muito grande! Por favor, envie um arquivo menor.\"}");
            httpResponse.setContentType("application/json");
            httpResponse.getWriter().flush();
        }
    }
}
