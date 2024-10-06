package org.example.library.repository;

import org.example.library.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BrandRepository extends JpaRepository<Brand, Long> {

    //    Search by keyword
    @Query("select b from Brand b where b.name like %?1%")
    public List<Brand> findByKeyword(String keyword);


}
