package org.example.library.repository;

import org.example.library.dto.CategorySalesDto;
import org.example.library.dto.StatisticalCategoryDto;
import org.example.library.model.CategoryIns;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryInsRepository extends JpaRepository<CategoryIns, Long> {

}
