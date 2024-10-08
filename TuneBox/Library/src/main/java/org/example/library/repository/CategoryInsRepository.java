package org.example.library.repository;

import org.example.library.model.CategoryIns;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryInsRepository extends JpaRepository<CategoryIns, Long> {

    //    List category use sort in shop
    @Query("select c.id, c.name from CategoryIns c where c.status = false")
    public List<CategoryIns> getSortedCategories();
}
