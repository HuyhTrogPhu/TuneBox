package org.example.library.repository;

import org.example.library.model.Instrument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InstrumentRepository extends JpaRepository<Instrument, Long> {

    @Query("select i from Instrument i join i.brand join i.categoryIns " +
            "where i.name like %?1% or i.brand.name like %?1% " +
            "or i.categoryIns.name like %?1%")
    public List<Instrument> findByKeyword(String keyword);

    List<Instrument> findByBrandId(Long brandId);
}
