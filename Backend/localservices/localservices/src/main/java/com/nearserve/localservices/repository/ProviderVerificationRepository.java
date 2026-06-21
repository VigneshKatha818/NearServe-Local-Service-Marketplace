package com.nearserve.localservices.repository;

import com.nearserve.localservices.entity.ProviderVerification;
import com.nearserve.localservices.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProviderVerificationRepository
        extends JpaRepository<ProviderVerification, Long> {

    Optional<ProviderVerification> findByProvider(User provider);
}