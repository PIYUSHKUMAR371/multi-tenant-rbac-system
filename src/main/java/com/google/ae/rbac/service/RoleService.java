package com.google.ae.rbac.service;

import com.google.ae.rbac.dto.AssignRoleRequest;
import com.google.ae.rbac.dto.CreateRoleRequest;
import com.google.ae.rbac.exception.ResourceNotFoundException;
import com.google.ae.rbac.model.Permission;
import com.google.ae.rbac.model.Role;
import com.google.ae.rbac.model.Tenant;
import com.google.ae.rbac.model.User;
import com.google.ae.rbac.repository.PermissionRepository;
import com.google.ae.rbac.repository.RoleRepository;
import com.google.ae.rbac.repository.TenantRepository;
import com.google.ae.rbac.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final TenantRepository tenantRepository;
    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;

    public RoleService(RoleRepository roleRepository, TenantRepository tenantRepository,
                       PermissionRepository permissionRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.tenantRepository = tenantRepository;
        this.permissionRepository = permissionRepository;
        this.userRepository = userRepository;
    }

    public Role createRole(CreateRoleRequest request) {
        Tenant tenant = tenantRepository.findByTenantCode(request.getTenantCode())
                .orElseThrow(() -> new ResourceNotFoundException("Tenant not found with code: " + request.getTenantCode()));

        Set<Permission> permissions = new HashSet<>();
        if (request.getPermissionNames() != null) {
            for (String permName : request.getPermissionNames()) {
                Permission perm = permissionRepository.findByName(permName)
                        .orElseThrow(() -> new ResourceNotFoundException("Permission not found: " + permName));
                permissions.add(perm);
            }
        }

        Role role = Role.builder()
                .name(request.getRoleName())
                .tenant(tenant)
                .permissions(permissions)
                .build();

        return roleRepository.save(role);
    }

    public String assignRoleToUser(AssignRoleRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.getUsername()));

        Tenant tenant = tenantRepository.findByTenantCode(request.getTenantCode())
                .orElseThrow(() -> new ResourceNotFoundException("Tenant not found with code: " + request.getTenantCode()));

        // Enforce Multi-Tenant Data Isolation Check
        if (!user.getTenant().getTenantCode().equals(tenant.getTenantCode())) {
            throw new RuntimeException("Cross-Tenant Error: User does not belong to tenant " + request.getTenantCode());
        }

        Role role = roleRepository.findByNameAndTenant(request.getRoleName(), tenant)
                .orElseThrow(() -> new ResourceNotFoundException("Role " + request.getRoleName() + " not found for Tenant: " + request.getTenantCode()));

        user.getRoles().add(role);
        userRepository.save(user);

        return "Role '" + request.getRoleName() + "' assigned to user '" + request.getUsername() + "' successfully!";
    }

    public List<Role> getRolesByTenant(String tenantCode) {
        Tenant tenant = tenantRepository.findByTenantCode(tenantCode)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant not found with code: " + tenantCode));
        return roleRepository.findByTenant(tenant);
    }
}
