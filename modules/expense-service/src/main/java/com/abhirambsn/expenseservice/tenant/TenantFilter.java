package com.abhirambsn.expenseservice.tenant;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@AllArgsConstructor
@Slf4j
@Component
@Order(1)
public class TenantFilter extends OncePerRequestFilter {
    private static final String TENANT_HEADER = "X-TenantID";

    @Autowired
    private TenantContext tenantContext;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            final String tenant = extractTenantId(request);
            log.info("Extracted Tenant ID: {}", tenant);
            if (!tenant.isEmpty()) {
                tenantContext.setTenantId(tenant);
                log.info("Getting request from tenant: {}", tenant);
            } else {
//                throw new TenantNotFoundException("Tenant not found");
                handleNoTenant(request);
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            logger.error("Error processing tenant context", e);
            throw new ServletException("Tenant processing error", e);
        } finally {
            tenantContext.clear();
        }
    }

    private String extractTenantId(HttpServletRequest request) {
        String tenantId = request.getHeader(TENANT_HEADER);

        if (tenantId == null || tenantId.isEmpty()) {
            tenantId = request.getParameter("tenant");
        }
        if (tenantId == null || tenantId.isEmpty()) {
            tenantId = "main";
        }

        return tenantId;
    }

    private void handleNoTenant(HttpServletRequest request) {
        log.warn("No tenant identified for request: {} {}",
                request.getMethod(),
                request.getRequestURI()
        );
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.startsWith("/public") ||
                path.startsWith("/actuator") ||
                path.startsWith("/swagger");
    }
}
