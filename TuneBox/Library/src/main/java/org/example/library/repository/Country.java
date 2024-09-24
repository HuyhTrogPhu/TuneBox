package org.example.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface Country extends JpaRepository<Country, Long> {
}
