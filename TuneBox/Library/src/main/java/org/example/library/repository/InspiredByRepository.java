package org.example.library.repository;

import org.example.library.model.InspiredBy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InspiredByRepository extends JpaRepository<InspiredBy, Long> {
}
