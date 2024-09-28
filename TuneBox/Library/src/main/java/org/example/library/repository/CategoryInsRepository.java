package org.example.library.repository;

import org.example.library.model.CategoryIns;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryInsRepository extends JpaRepository<CategoryIns, Long> {

}
