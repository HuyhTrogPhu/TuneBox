package org.example.library.repository;

import org.example.library.dto.InstrumentSalesDto;
import org.example.library.dto.StatisticalInstrumentDto;
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

    // list id and name instrument
    @Query("select new org.example.library.dto.StatisticalInstrumentDto(i.id, i.name) from Instrument i ")
    List<StatisticalInstrumentDto> getStatisticalInstruments();


    // Bán chạy nhất trong ngày
    @Query("SELECT new org.example.library.dto.InstrumentSalesDto(i.id, i.name, SUM(od.quantity)) " +
            "FROM Order o JOIN o.orderDetails od JOIN od.instrument i " +
            "WHERE o.orderDate = CURRENT_DATE " +
            "GROUP BY i.id, i.name " +
            "ORDER BY SUM(od.quantity) DESC")
    List<InstrumentSalesDto> getInstrumentSalesTheMostOfDay();


    // Bán chạy nhất trong tuần
    @Query("SELECT new org.example.library.dto.InstrumentSalesDto(i.id, i.name, SUM(od.quantity)) " +
            "FROM Order o JOIN o.orderDetails od JOIN od.instrument i " +
            "WHERE YEAR(o.orderDate) = YEAR(CURRENT_DATE) AND WEEK(o.orderDate) = WEEK(CURRENT_DATE) " +
            "GROUP BY i.id, i.name " +
            "ORDER BY SUM(od.quantity) DESC")
    List<InstrumentSalesDto> getInstrumentSalesTheMostOfWeek();


    // Bán chạy nhất trong tháng
    @Query("SELECT new org.example.library.dto.InstrumentSalesDto(i.id, i.name, SUM(od.quantity)) " +
            "FROM Order o JOIN o.orderDetails od JOIN od.instrument i " +
            "WHERE MONTH(o.orderDate) = MONTH(CURRENT_DATE) AND YEAR(o.orderDate) = YEAR(CURRENT_DATE) " +
            "GROUP BY i.id, i.name " +
            "ORDER BY SUM(od.quantity) DESC")
    List<InstrumentSalesDto> getInstrumentSalesTheMostOfMonth();




    // Bán ít nhất trong ngày
    @Query("SELECT new org.example.library.dto.InstrumentSalesDto(i.id, i.name, SUM(od.quantity)) " +
            "FROM Instrument i LEFT JOIN OrderDetail od ON i.id = od.instrument.id " +
            "LEFT JOIN od.order o " +
            "WHERE (o.orderDate = CURRENT_DATE OR o.orderDate IS NULL) " +
            "GROUP BY i.id, i.name " +
            "ORDER BY SUM(od.quantity) ASC")
    List<InstrumentSalesDto> getInstrumentSalesTheLeastOfDay();


    // Bán ít nhất trong tuần
    @Query("SELECT new org.example.library.dto.InstrumentSalesDto(i.id, i.name, SUM(od.quantity)) " +
            "FROM Instrument i LEFT JOIN OrderDetail od ON i.id = od.instrument.id " +
            "LEFT JOIN od.order o " +
            "WHERE (YEAR(o.orderDate) = YEAR(CURRENT_DATE) AND WEEK(o.orderDate) = WEEK(CURRENT_DATE)) OR o.orderDate IS NULL " +
            "GROUP BY i.id, i.name " +
            "ORDER BY SUM(od.quantity) ASC")
    List<InstrumentSalesDto> getInstrumentSalesTheLeastOfWeek();


    // Bán ít nhất trong tháng
    @Query("SELECT new org.example.library.dto.InstrumentSalesDto(i.id, i.name, SUM(od.quantity)) " +
            "FROM Instrument i LEFT JOIN OrderDetail od ON i.id = od.instrument.id " +
            "LEFT JOIN od.order o " +
            "WHERE (MONTH(o.orderDate) = MONTH(CURRENT_DATE) AND YEAR(o.orderDate) = YEAR(CURRENT_DATE)) OR o.orderDate IS NULL " +
            "GROUP BY i.id, i.name " +
            "ORDER BY SUM(od.quantity) ASC")
    List<InstrumentSalesDto> getInstrumentSalesTheLeastOfMonth();



}

