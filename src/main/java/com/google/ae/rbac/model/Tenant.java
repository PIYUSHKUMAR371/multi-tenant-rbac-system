package com.google.ae.rbac.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tenants")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String tenantCode;

    @Column(nullable = false)
    private String organizationName;

    private boolean active = true;

    private LocalDateTime createdAt = LocalDateTime.now();
}