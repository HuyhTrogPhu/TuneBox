package org.example.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.example.library.model.Country;
public interface CountryRepository extends JpaRepository<Country, Long> {
}
