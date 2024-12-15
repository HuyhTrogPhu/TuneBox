package org.example.library.repository;

import org.example.library.dto.InstrumentAccordingTo;
import org.example.library.dto.InstrumentSalesDto;
import org.example.library.dto.StatisticalInstrumentDto;
import org.example.library.model.Instrument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface InstrumentRepository extends JpaRepository<Instrument, Long> {

    @Query("select i from Instrument i where i.brand.id = ?1")
    List<Instrument> findByBrandId(Long brandId);

    List<Instrument> findByCategoryInsId(Long categoryId);


    List<Instrument> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrColorContainingIgnoreCase(String keyword, String keyword2, String keyword3);

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
    @Query("SELECT new org.example.library.dto.InstrumentSalesDto(i.id, i.name, i.image, i.costPrice, i.quantity, SUM(od.quantity)) " +
            "FROM Order o JOIN o.orderDetails od JOIN od.instrument i " +
            "WHERE o.orderDate = CURRENT_DATE " +
            "GROUP BY i.id, i.name, i.image, i.costPrice, i.quantity " +
            "ORDER BY SUM(od.quantity) DESC")
    List<InstrumentSalesDto> getInstrumentSalesTheMostOfDay();


    // Bán chạy nhất trong tuần
    @Query("SELECT new org.example.library.dto.InstrumentSalesDto(i.id, i.name, i.image, i.costPrice, i.quantity, SUM(od.quantity)) " +
            "FROM Order o JOIN o.orderDetails od JOIN od.instrument i " +
            "WHERE YEAR(o.orderDate) = YEAR(CURRENT_DATE) AND WEEK(o.orderDate) = WEEK(CURRENT_DATE) " +
            "GROUP BY i.id, i.name, i.image, i.costPrice, i.quantity " +
            "ORDER BY SUM(od.quantity) DESC")
    List<InstrumentSalesDto> getInstrumentSalesTheMostOfWeek();


    // Bán chạy nhất trong tháng
    @Query("SELECT new org.example.library.dto.InstrumentSalesDto(i.id, i.name, i.image, i.costPrice, i.quantity, SUM(od.quantity)) " +
            "FROM Order o JOIN o.orderDetails od JOIN od.instrument i " +
            "WHERE MONTH(o.orderDate) = MONTH(CURRENT_DATE) AND YEAR(o.orderDate) = YEAR(CURRENT_DATE) " +
            "GROUP BY i.id, i.name, i.image, i.costPrice, i.quantity " +
            "ORDER BY SUM(od.quantity) DESC")
    List<InstrumentSalesDto> getInstrumentSalesTheMostOfMonth();


    // Bán ít nhất trong ngày
    @Query("SELECT new org.example.library.dto.InstrumentSalesDto(i.id, i.name, i.image, i.costPrice, i.quantity, SUM(od.quantity)) " +
            "FROM Instrument i LEFT JOIN OrderDetail od ON i.id = od.instrument.id " +
            "LEFT JOIN od.order o " +
            "WHERE (o.orderDate = CURRENT_DATE OR o.orderDate IS NULL) " +
            "GROUP BY i.id, i.name, i.image, i.costPrice, i.quantity " +
            "ORDER BY SUM(od.quantity) ASC")
    List<InstrumentSalesDto> getInstrumentSalesTheLeastOfDay();


    // Bán ít nhất trong tuần
    @Query("SELECT new org.example.library.dto.InstrumentSalesDto(i.id, i.name, i.image, i.costPrice, i.quantity, SUM(od.quantity)) " +
            "FROM Instrument i LEFT JOIN OrderDetail od ON i.id = od.instrument.id " +
            "LEFT JOIN od.order o " +
            "WHERE (YEAR(o.orderDate) = YEAR(CURRENT_DATE) AND WEEK(o.orderDate) = WEEK(CURRENT_DATE)) OR o.orderDate IS NULL " +
            "GROUP BY i.id, i.name, i.image, i.costPrice, i.quantity " +
            "ORDER BY SUM(od.quantity) ASC")
    List<InstrumentSalesDto> getInstrumentSalesTheLeastOfWeek();


    // Bán ít nhất trong tháng
    @Query("SELECT new org.example.library.dto.InstrumentSalesDto(i.id, i.name, i.image, i.costPrice, i.quantity, SUM(od.quantity)) " +
            "FROM Instrument i LEFT JOIN OrderDetail od ON i.id = od.instrument.id " +
            "LEFT JOIN od.order o " +
            "WHERE (MONTH(o.orderDate) = MONTH(CURRENT_DATE) AND YEAR(o.orderDate) = YEAR(CURRENT_DATE)) OR o.orderDate IS NULL " +
            "GROUP BY i.id, i.name, i.image, i.costPrice, i.quantity " +
            "ORDER BY SUM(od.quantity) ASC")
    List<InstrumentSalesDto> getInstrumentSalesTheLeastOfMonth();


    // Doanh thu của instrument theo ngày hiện tại
    @Query("SELECT SUM(oi.quantity * i.costPrice) FROM Order o " +
            "JOIN o.orderDetails oi " +
            "JOIN oi.instrument i " +
            "WHERE i.id = :instrumentId AND o.orderDate = CURRENT_DATE")
    Double getTotalRevenueInstrumentOfDay(@Param("instrumentId") Long instrumentId);

    // Doanh thu của instrument theo tuần hiện tại
    @Query("SELECT SUM(oi.quantity * i.costPrice) FROM Order o " +
            "JOIN o.orderDetails oi " +
            "JOIN oi.instrument i " +
            "WHERE i.id = :instrumentId AND FUNCTION('YEARWEEK', o.orderDate, 1) = FUNCTION('YEARWEEK', CURRENT_DATE, 1)")
    Double getTotalRevenueInstrumentOfWeek(@Param("instrumentId") Long instrumentId);

    // Doanh thu của instrument theo tháng hiện tại
    @Query("SELECT SUM(oi.quantity * i.costPrice) FROM Order o " +
            "JOIN o.orderDetails oi " +
            "JOIN oi.instrument i " +
            "WHERE i.id = :instrumentId AND FUNCTION('YEAR', o.orderDate) = FUNCTION('YEAR', CURRENT_DATE) " +
            "AND FUNCTION('MONTH', o.orderDate) = FUNCTION('MONTH', CURRENT_DATE)")
    Double getTotalRevenueInstrumentOfMonth(@Param("instrumentId") Long instrumentId);

    // Doanh thu của instrument theo năm hiện tại
    @Query("SELECT SUM(oi.quantity * i.costPrice) FROM Order o " +
            "JOIN o.orderDetails oi " +
            "JOIN oi.instrument i " +
            "WHERE i.id = :instrumentId AND FUNCTION('YEAR', o.orderDate) = FUNCTION('YEAR', CURRENT_DATE)")
    Double getTotalRevenueInstrumentOfYear(@Param("instrumentId") Long instrumentId);


    // get list instrument sell by day
    @Query("select new org.example.library.dto.InstrumentAccordingTo(i.id, i.name, i.costPrice, i.image, sum(od.quantity)) " +
            "from Instrument i left join OrderDetail od on i.id = od.instrument.id " +
            "left join od.order o " +
            "where o.orderDate = :date " +
            "group by i.id, i.name, i.costPrice, i.image " +
            "order by sum(od.quantity) desc")
    List<InstrumentAccordingTo> getInstrumentAccordingToDay(@Param("date") LocalDate date);


    // get list instrument sell between date
    @Query("select new org.example.library.dto.InstrumentAccordingTo(i.id, i.name, i.costPrice, i.image, sum(od.quantity)) " +
            "from Instrument i left join OrderDetail od on i.id = od.instrument.id " +
            "left join od.order o " +
            "where o.orderDate between :startDate and :endDate " +
            "group by i.id, i.name, i.costPrice, i.image " +
            "order by sum(od.quantity) desc")
    List<InstrumentAccordingTo> getInstrumentBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);


    // get list instrument sell by week
    @Query("select new org.example.library.dto.InstrumentAccordingTo(i.id, i.name, i.costPrice, i.image, sum(od.quantity)) " +
            "from Instrument i left join OrderDetail od on i.id = od.instrument.id " +
            "left join od.order o " +
            "where YEARWEEK(o.orderDate, 1) = YEARWEEK(:date, 1) " +
            "group by i.id, i.name, i.costPrice, i.image " +
            "order by sum(od.quantity) desc")
    List<InstrumentAccordingTo> getInstrumentByWeek(@Param("date") LocalDate date);


    // get list instrument between week
    @Query("select new org.example.library.dto.InstrumentAccordingTo(i.id, i.name, i.costPrice, i.image, sum(od.quantity)) " +
            "from Instrument i left join OrderDetail od on i.id = od.instrument.id " +
            "left join od.order o " +
            "where (YEAR(o.orderDate) = YEAR(:startDate) and WEEK(o.orderDate) >= WEEK(:startDate)) " +
            "   or (YEAR(o.orderDate) = YEAR(:endDate) and WEEK(o.orderDate) <= WEEK(:endDate)) " +
            "group by i.id, i.name, i.costPrice, i.image " +
            "order by sum(od.quantity) desc")
    List<InstrumentAccordingTo> getInstrumentBetweenWeek(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);


    // get list instrument by month
    @Query("select new org.example.library.dto.InstrumentAccordingTo(i.id, i.name, i.costPrice, i.image, sum(od.quantity)) " +
            "from Instrument i left join OrderDetail od on i.id = od.instrument.id " +
            "left join od.order o " +
            "where YEAR(o.orderDate) = :year AND MONTH(o.orderDate) = :month " +
            "group by i.id, i.name, i.costPrice, i.image " +
            "order by sum(od.quantity) desc")
    List<InstrumentAccordingTo> getInstrumentsByMonth(@Param("year") int year, @Param("month") int month);


    // get list instrument between month
    @Query("select new org.example.library.dto.InstrumentAccordingTo(i.id, i.name, i.costPrice, i.image, sum(od.quantity)) " +
            "from Instrument i left join OrderDetail od on i.id = od.instrument.id " +
            "left join od.order o " +
            "where YEAR(o.orderDate) = :year AND MONTH(o.orderDate) BETWEEN :startMonth AND :endMonth " +
            "group by i.id, i.name, i.costPrice, i.image " +
            "order by sum(od.quantity) desc")
    List<InstrumentAccordingTo> getInstrumentsBetweenMonths(@Param("year") int year, @Param("startMonth") int startMonth, @Param("endMonth") int endMonth);

    // get list instrument by year
    @Query("SELECT new org.example.library.dto.InstrumentAccordingTo(i.id, i.name, i.costPrice, i.image, SUM(od.quantity)) " +
            "FROM Instrument i LEFT JOIN OrderDetail od ON i.id = od.instrument.id " +
            "LEFT JOIN od.order o " +
            "WHERE YEAR(o.orderDate) = :year " +
            "GROUP BY i.id, i.name, i.costPrice, i.image " +
            "ORDER BY SUM(od.quantity) DESC")
    List<InstrumentAccordingTo> getInstrumentByYear(@Param("year") int year);


    // get list instrument between years
    @Query("SELECT new org.example.library.dto.InstrumentAccordingTo(i.id, i.name, i.costPrice, i.image, SUM(od.quantity)) " +
            "FROM Instrument i LEFT JOIN OrderDetail od ON i.id = od.instrument.id " +
            "LEFT JOIN od.order o " +
            "WHERE YEAR(o.orderDate) BETWEEN :startYear AND :endYear " +
            "GROUP BY i.id, i.name, i.costPrice, i.image " +
            "ORDER BY SUM(od.quantity) DESC")
    List<InstrumentAccordingTo> getInstrumentBetweenYears(@Param("startYear") int startYear, @Param("endYear") int endYear);



}

