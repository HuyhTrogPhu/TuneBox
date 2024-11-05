package org.example.library.repository;

import org.example.library.dto.BrandSalesDto;
import org.example.library.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BrandRepository extends JpaRepository<Brand, Long> {

    //    Search by keyword
    @Query("select b from Brand b where b.name like %?1%")
    public List<Brand> findByKeyword(String keyword);

//    // get brand sales the most of day
//    @Query("SELECT new org.example.library.dto.BrandSalesDto(b.id, b.name, COALESCE(SUM(od.quantity), 0)) " +
//            "FROM Brand b LEFT JOIN Instrument i ON b.id = i.brand.id " +
//            "LEFT JOIN OrderDetail od ON i.id = od.instrument.id " +
//            "LEFT JOIN Order o ON od.order.id = o.id " +
//            "WHERE (o.orderDate = CURRENT_DATE OR o.orderDate IS NULL) " +
//            "GROUP BY b.id, b.name " +
//            "ORDER BY COALESCE(SUM(od.quantity), 0) DESC")
//    BrandSalesDto getBrandSalesTheMostOfDay();
//
//    // get brand sales the most of week
//    @Query("SELECT new org.example.library.dto.BrandSalesDto(b.id, b.name, COALESCE(SUM(od.quantity), 0)) " +
//            "FROM Brand b LEFT JOIN Instrument i ON b.id = i.brand.id " +
//            "LEFT JOIN OrderDetail od ON i.id = od.instrument.id " +
//            "LEFT JOIN Order o ON od.order.id = o.id " +
//            "WHERE (o.orderDate >= CURRENT_DATE - INTERVAL '7 days' OR o.orderDate IS NULL) " +
//            "GROUP BY b.id, b.name " +
//            "ORDER BY COALESCE(SUM(od.quantity), 0) DESC")
//    BrandSalesDto getBrandSalesTheMostOfWeek();
//
//    // get brand sales the most of month
//    @Query("SELECT new org.example.library.dto.BrandSalesDto(b.id, b.name, COALESCE(SUM(od.quantity), 0)) " +
//            "FROM Brand b LEFT JOIN Instrument i ON b.id = i.brand.id " +
//            "LEFT JOIN OrderDetail od ON i.id = od.instrument.id " +
//            "LEFT JOIN Order o ON od.order.id = o.id " +
//            "WHERE (o.orderDate >= CURRENT_DATE - INTERVAL '30 days' OR o.orderDate IS NULL) " +
//            "GROUP BY b.id, b.name " +
//            "ORDER BY COALESCE(SUM(od.quantity), 0) DESC")
//    BrandSalesDto getBrandSalesTheMostOfMonth();
//
//    // get brand sales the least of day
//    @Query("SELECT new org.example.library.dto.BrandSalesDto(b.id, b.name, COALESCE(SUM(od.quantity), 0)) " +
//            "FROM Brand b LEFT JOIN Instrument i ON b.id = i.brand.id " +
//            "LEFT JOIN OrderDetail od ON i.id = od.instrument.id " +
//            "LEFT JOIN Order o ON od.order.id = o.id " +
//            "WHERE (o.orderDate = CURRENT_DATE OR o.orderDate IS NULL) " +
//            "GROUP BY b.id, b.name " +
//            "ORDER BY COALESCE(SUM(od.quantity), 0) ASC")
//    BrandSalesDto getBrandSalesTheLeastOfDay();
//
//    // get brand sales the least of week
//    @Query("SELECT new org.example.library.dto.BrandSalesDto(b.id, b.name, COALESCE(SUM(od.quantity), 0)) " +
//            "FROM Brand b LEFT JOIN Instrument i ON b.id = i.brand.id " +
//            "LEFT JOIN OrderDetail od ON i.id = od.instrument.id " +
//            "LEFT JOIN Order o ON od.order.id = o.id " +
//            "WHERE (o.orderDate >= CURRENT_DATE - INTERVAL '7 days' OR o.orderDate IS NULL) " +
//            "GROUP BY b.id, b.name " +
//            "ORDER BY COALESCE(SUM(od.quantity), 0) ASC")
//    BrandSalesDto getBrandSalesTheLeastOfWeek();
//
//    // get brand sales the least of month
//    @Query("SELECT new org.example.library.dto.BrandSalesDto(b.id, b.name, COALESCE(SUM(od.quantity), 0)) " +
//            "FROM Brand b LEFT JOIN Instrument i ON b.id = i.brand.id " +
//            "LEFT JOIN OrderDetail od ON i.id = od.instrument.id " +
//            "LEFT JOIN Order o ON od.order.id = o.id " +
//            "WHERE (o.orderDate >= CURRENT_DATE - INTERVAL '30 days' OR o.orderDate IS NULL) " +
//            "GROUP BY b.id, b.name " +
//            "ORDER BY COALESCE(SUM(od.quantity), 0) ASC")
//    BrandSalesDto getBrandSalesTheLeastOfMonth();
}

