package org.example.library.repository;

import org.example.library.model.EcommerceAdmin;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EcommerceAdminRepository extends JpaRepository<EcommerceAdmin, Long> {
    Optional<EcommerceAdmin> findByEmail(String email);

}
