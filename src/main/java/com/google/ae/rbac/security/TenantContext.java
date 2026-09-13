package com.google.ae.rbac.security;

public class TenantContext {

    // ThreadLocal stores tenant data scoped exclusively to the current execution thread
    private static final ThreadLocal<String> CURRENT_TENANT = new ThreadLocal<>();

    public static void setCurrentTenant(String tenantCode) {
        CURRENT_TENANT.set(tenantCode);
    }

    public static String getCurrentTenant() {
        return CURRENT_TENANT.get();
    }

    // Always clear the thread context after request completion to prevent memory leaks in web servers
    public static void clear() {
        CURRENT_TENANT.remove();
    }
}