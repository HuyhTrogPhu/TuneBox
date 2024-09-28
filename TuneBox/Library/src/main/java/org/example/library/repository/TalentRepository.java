package org.example.library.repository;

import org.example.library.model.InspiredBy;
import org.example.library.model.Talent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TalentRepository extends JpaRepository<Talent, Long> {
    List<Talent> findByName(String name);
}
