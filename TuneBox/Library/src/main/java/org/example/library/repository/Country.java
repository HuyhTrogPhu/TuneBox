package org.example.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface Country extends JpaRepository<org.example.library.model.Country, Long> {
}
