package com.stacktobasics.correlationidsspringboot3.infra.web;

import io.micrometer.tracing.Tracer;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;

import org.apache.logging.log4j.ThreadContext;

@Component
@Slf4j
public class TraceFilter implements Filter {

    private static final String TRACE_ID_HEADER_NAME = "correlationid";
    public static final String DEFAULT = "00";
    private final Tracer tracer;

    public TraceFilter(Tracer tracer) {
        this.tracer = tracer;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
            FilterChain chain) throws IOException, ServletException {
        try {
            logIncomingRequest((HttpServletRequest) req);
            HttpServletResponse response = (HttpServletResponse) res;
            var context = tracer.currentTraceContext().context();
            var traceId = context.traceId();
            var parentId = context.spanId();
            var correlationId = traceId + "-" + parentId;
            ThreadContext.put("correlationId", correlationId);
            response.setHeader(TRACE_ID_HEADER_NAME, correlationId);
            chain.doFilter(req, res);
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            ThreadContext.remove("correlationId");
        }

    }

    private void logIncomingRequest(HttpServletRequest request) throws IOException {
        log.info("Incoming Request URI: {}", request.getRequestURI());
        log.info("Incoming Request Method: {}", request.getMethod());
        log.info("Incoming Request Parameters: {}", request.getParameterMap());

        // Print all request headers
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            log.info("Incoming Request Header - {}: {}", headerName, headerValue);
        }

        // Read and print the request body
        StringBuilder requestBody = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
        }
        log.info("Incoming Request Body: {}", JsonUtils.prettyPrintJson(requestBody.toString()));
    }
}
