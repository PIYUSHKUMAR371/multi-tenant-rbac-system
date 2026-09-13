package com.google.ae.rbac.service;

import com.google.ae.rbac.exception.ResourceNotFoundException;
import com.google.ae.rbac.model.Tenant;
import com.google.ae.rbac.repository.TenantRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TenantService {

    private final TenantRepository tenantRepository;

    public TenantService(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    public List<Tenant> getAllTenants() {
        return tenantRepository.findAll();
    }

    public Tenant getTenantByCode(String tenantCode) {
        return tenantRepository.findByTenantCode(tenantCode)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant not found with code: " + tenantCode));
    }
}