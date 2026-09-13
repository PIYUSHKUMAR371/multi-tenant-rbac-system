package com.google.ae.rbac.controller;

import com.google.ae.rbac.dto.AssignRoleRequest;
import com.google.ae.rbac.dto.CreateRoleRequest;
import com.google.ae.rbac.model.Role;
import com.google.ae.rbac.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    // Endpoint 1: Create a new custom role with specific permissions for a tenant
    @PostMapping("/create")
    public ResponseEntity<Role> createRole(@RequestBody CreateRoleRequest request) {
        Role createdRole = roleService.createRole(request);
        return ResponseEntity.ok(createdRole);
    }

    // Endpoint 2: Assign an existing role to a user within a tenant space
    @PostMapping("/assign")
    public ResponseEntity<String> assignRole(@RequestBody AssignRoleRequest request) {
        String response = roleService.assignRoleToUser(request);
        return ResponseEntity.ok(response);
    }

    // Endpoint 3: Fetch all roles configured for a specific tenant
    @GetMapping("/tenant/{tenantCode}")
    public ResponseEntity<List<Role>> getTenantRoles(@PathVariable String tenantCode) {
        List<Role> roles = roleService.getRolesByTenant(tenantCode);
        return ResponseEntity.ok(roles);
    }
}