package com.google.ae.rbac.controller;

import com.google.ae.rbac.model.Tenant;
import com.google.ae.rbac.service.TenantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tenants")
public class TenantController {

    private final TenantService tenantService;

    public TenantController(TenantService tenantService) {
        this.tenantService = tenantService;
    }

    @GetMapping
    public ResponseEntity<List<Tenant>> getAllTenants() {
        List<Tenant> tenants = tenantService.getAllTenants();
        return ResponseEntity.ok(tenants);
    }

    @GetMapping("/{tenantCode}")
    public ResponseEntity<Tenant> getTenantByCode(@PathVariable String tenantCode) {
        Tenant tenant = tenantService.getTenantByCode(tenantCode);
        return ResponseEntity.ok(tenant);
    }
}