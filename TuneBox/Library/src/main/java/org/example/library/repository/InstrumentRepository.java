package org.example.library.repository;

import org.example.library.model.Instrument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InstrumentRepository extends JpaRepository<Instrument, Long> {

    @Query("select i from Instrument i where i.brand.id = ?1")
    List<Instrument> findByBrandId(Long brandId);

    List<Instrument> findByCategoryInsId(Long categoryId);


    //    List instrument use in shop
    @Query("select i.id, i.name from Instrument i where i.status = false")
    public List<Instrument> getSortedInstruments();

    //    List instrument by category id and brand id
    @Query("select i from Instrument i join fetch i.brand b join fetch i.categoryIns c " +
            "where c.id = ?1 and b.id = ?2")
    public List<Instrument> getInstrumentByCategoryIdAndBrandId(Long categoryId, Long brandId);

}
