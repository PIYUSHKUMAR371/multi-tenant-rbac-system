package com.google.ae.rbac.dto;

import lombok.Data;

@Data
public class AssignRoleRequest {
    private String username;
    private String roleName;
    private String tenantCode;
}