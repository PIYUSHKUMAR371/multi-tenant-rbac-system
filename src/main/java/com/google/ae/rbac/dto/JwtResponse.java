package com.google.ae.rbac.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String username;
    private String tenantCode;
    private List<String> roles;
}
