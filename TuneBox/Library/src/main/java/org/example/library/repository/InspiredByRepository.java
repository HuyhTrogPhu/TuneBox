package org.example.library.repository;

import org.example.library.model.InspiredBy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface InspiredByRepository extends JpaRepository<InspiredBy, Long> {

}
