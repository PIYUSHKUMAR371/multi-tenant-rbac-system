package com.google.ae.rbac.config;

import com.google.ae.rbac.model.Permission;
import com.google.ae.rbac.repository.PermissionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final PermissionRepository permissionRepository;

    public DataInitializer(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        List<String> defaultPermissions = List.of(
                "USER_READ",
                "USER_WRITE",
                "USER_DELETE",
                "ROLE_READ",
                "ROLE_WRITE",
                "TENANT_READ",
                "TENANT_WRITE"
        );

        for (String permName : defaultPermissions) {
            if (permissionRepository.findByName(permName).isEmpty()) {
                permissionRepository.save(
                        Permission.builder()
                                .name(permName)
                                .description("Permission to perform " + permName + " operations")
                                .build()
                );
            }
        }
    }
}