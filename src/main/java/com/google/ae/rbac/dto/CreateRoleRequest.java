package com.google.ae.rbac.dto;

import lombok.Data;
import java.util.Set;

@Data
public class CreateRoleRequest {
    private String tenantCode;
    private String roleName;
    private Set<String> permissionNames; // e.g. ["USER_READ", "USER_WRITE"]
}