package com.nearserve.localservices.config;

import com.nearserve.localservices.entity.Role;
import com.nearserve.localservices.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    private final RoleRepository roleRepository;

    public DataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    public void initRoles() {

        if (roleRepository.findByName("ADMIN").isEmpty()) {
            roleRepository.save(new Role(null, "ADMIN"));
        }

        if (roleRepository.findByName("CUSTOMER").isEmpty()) {
            roleRepository.save(new Role(null, "CUSTOMER"));
        }

        if (roleRepository.findByName("SERVICE_PROVIDER").isEmpty()) {
            roleRepository.save(new Role(null, "SERVICE_PROVIDER"));
        }
    }
}