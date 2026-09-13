package com.google.ae.rbac.repository;

import com.google.ae.rbac.model.Role;
import com.google.ae.rbac.model.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByNameAndTenant(String name, Tenant tenant);
    List<Role> findByTenant(Tenant tenant);
}