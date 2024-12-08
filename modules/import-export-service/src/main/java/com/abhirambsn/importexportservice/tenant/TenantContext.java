package com.abhirambsn.importexportservice.tenant;

import org.springframework.stereotype.Component;

@Component
public class TenantContext {
    private final ThreadLocal<String> tenantId = new ThreadLocal<>();

    public String getTenantId() {
        return tenantId.get();
    }

    public void setTenantId(final String tenantId) {
        this.tenantId.set(tenantId);
    }

    public void clear() {
        tenantId.remove();
    }
}
