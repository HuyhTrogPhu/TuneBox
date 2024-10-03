package org.example.library.repository;

import org.example.library.model.Instrument;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;


public interface InstrumentRepository extends JpaRepository<Instrument, Long> {

//    Select all instrument
    @Query("select i from Instrument i join i.images")
    public List<Instrument> getAllInstrument();

    //    Search by keyword
    @Query("select i from Instrument i join i.brand join i.categoryIns " +
            "where i.name like %?1% or i.brand.name like %?1% " +
            "or i.categoryIns.name like %?1%")
    public List<Instrument> findByKeyword(String keyword);

    //    Search by brand
    @Query("select i from Instrument i join i.brand b " +
            "where b.id = :brandId")
    public List<Instrument> findByBrandId(Long brandId);


    // Search by category
    @Query("select i from Instrument i join i.categoryIns c " +
            "where c.id = :categoryId")
    public List<Instrument> findByCategoryId(Long categoryId);

    // Sort instrument name from a to z
    @Query("select i from Instrument i order by i.name asc")
    public List<Instrument> sortByNameAsc();

    // Sort instrument name from z to a
    @Query("select i from Instrument i order by i.name desc")
    public List<Instrument> sortByNameDesc();

    // Sort instrument price low to high
    @Query("select i from Instrument i order by i.costPrice asc")
    public List<Instrument> sortByCostPriceAsc();

    // Sort instrument price high to low
    @Query("select i from Instrument i order by i.costPrice desc")
    public List<Instrument> sortByCostPriceDesc();



}
