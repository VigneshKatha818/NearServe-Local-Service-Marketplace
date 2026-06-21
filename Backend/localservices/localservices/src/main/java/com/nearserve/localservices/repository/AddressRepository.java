package com.nearserve.localservices.repository;

import com.nearserve.localservices.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}