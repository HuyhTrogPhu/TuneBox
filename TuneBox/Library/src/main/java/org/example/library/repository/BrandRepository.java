package org.example.library.repository;

import org.example.library.dto.BrandSalesDto;
import org.example.library.dto.StatisticalBrandDto;
import org.example.library.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BrandRepository extends JpaRepository<Brand, Long> {

    //    Search by keyword
    @Query("select b from Brand b where b.name like %?1%")
    List<Brand> findByKeyword(String keyword);

    // get list brand sales the most of day
    @Query("select new org.example.library.dto.BrandSalesDto(b.id, b.name, i.name, i.image, i.costPrice, i.quantity, sum(od.quantity))" +
            "from Order o join o.orderDetails od join od.instrument i join i.brand b " +
            "where o.orderDate = current_date " +
            "group by b.id, b.name, i.name, i.image, i.costPrice, i.quantity " +
            "order by sum(od.quantity) desc ")
    List<BrandSalesDto> getBrandSalesTheMostOdDay();

    // get list brand sales the most of week
    @Query("select new org.example.library.dto.BrandSalesDto(b.id, b.name, i.name, i.image, i.costPrice, i.quantity, sum(od.quantity))" +
            "from Order o join o.orderDetails od join od.instrument i join i.brand b " +
            "where YEAR(o.orderDate) = YEAR(CURRENT_DATE) AND WEEK(o.orderDate) = WEEK(CURRENT_DATE) " +
            "group by b.id, b.name, i.name, i.image, i.costPrice, i.quantity " +
            "order by sum(od.quantity) desc ")
    List<BrandSalesDto> getBrandSalesTheMostOfWeek();

    // get list brand sales the most of month
    @Query("select new org.example.library.dto.BrandSalesDto(b.id, b.name, i.name, i.image, i.costPrice, i.quantity, sum(od.quantity))" +
            "from Order o join o.orderDetails od join od.instrument i join i.brand b " +
            "WHERE MONTH(o.orderDate) = MONTH(CURRENT_DATE) AND YEAR(o.orderDate) = YEAR(CURRENT_DATE) " +
            "group by b.id, b.name, i.name, i.image, i.costPrice, i.quantity " +
            "order by sum(od.quantity) desc")
    List<BrandSalesDto> getBrandSalesTheMostOfMonth();

    // get list brand sales the least of day
    @Query("select new org.example.library.dto.BrandSalesDto(b.id, b.name, i.name, i.image, i.costPrice, i.quantity, sum(od.quantity))" +
            "from Order o join o.orderDetails od join od.instrument i join i.brand b " +
            "where o.orderDate = current_date " +
            "group by b.id, b.name, i.name, i.image, i.costPrice, i.quantity " +
            "order by sum(od.quantity) asc")
    List<BrandSalesDto> getBrandSalesTheLeastOdDay();

    // get list brand sales the least of week
    @Query("select new org.example.library.dto.BrandSalesDto(b.id, b.name, i.name, i.image, i.costPrice, i.quantity, sum(od.quantity))" +
            "from Order o join o.orderDetails od join od.instrument i join i.brand b " +
            "where YEAR(o.orderDate) = YEAR(CURRENT_DATE) AND WEEK(o.orderDate) = WEEK(CURRENT_DATE) " +
            "group by b.id, b.name, i.name, i.image, i.costPrice, i.quantity " +
            "order by sum(od.quantity) asc ")
    List<BrandSalesDto> getBrandSalesTheLeastOfWeek();

    // get list brand sales the least of month
    @Query("select new org.example.library.dto.BrandSalesDto(b.id, b.name, i.name, i.image, i.costPrice, i.quantity, sum(od.quantity))" +
            "from Order o join o.orderDetails od join od.instrument i join i.brand b " +
            "where MONTH(o.orderDate) = MONTH(CURRENT_DATE) AND YEAR(o.orderDate) = YEAR(CURRENT_DATE) " +
            "group by b.id, b.name, i.name, i.image, i.costPrice, i.quantity " +
            "order by sum(od.quantity) asc ")
    List<BrandSalesDto> getBrandSalesTheLeastOfMonth();

    // get id and name brand
    @Query("select new org.example.library.dto.StatisticalBrandDto(b.id, b.name) " +
            "from Brand b")
    List<StatisticalBrandDto> getStatisticalBrand();

    // get revenue brand by brand id of day
    @Query("select sum(od.quantity * i.costPrice) from Order o " +
            "join o.orderDetails od " +
            "join od.instrument i " +
            "join i.brand b " +
            "where b.id = :brandId AND FUNCTION('YEARWEEK', o.orderDate, 1) = FUNCTION('YEARWEEK', CURRENT_DATE, 1)")
    Double getRevenueByBrandIdOfDay(@Param("brandId") Long brandId);

    // get revenue brand by brand id of week
    @Query("select sum(od.quantity * i.costPrice) from Order o " +
            "join o.orderDetails od " +
            "join od.instrument i " +
            "join i.brand b " +
            "where b.id = :brandId AND FUNCTION('YEARWEEK', o.orderDate, 1) = FUNCTION('YEARWEEK', CURRENT_DATE, 1)")
    Double getRevenueByBrandIdOfWeek(@Param("brandId") Long brandId);

    // get revenue brand by brand id of month
    @Query("select sum(od.quantity * i.costPrice) from Order o " +
            "join o.orderDetails od " +
            "join od.instrument i " +
            "join i.brand b " +
            "where b.id = :brandId AND FUNCTION('YEAR', o.orderDate) = FUNCTION('YEAR', CURRENT_DATE) " +
            "AND FUNCTION('MONTH', o.orderDate) = FUNCTION('MONTH', CURRENT_DATE)")
    Double getRevenueByBrandIdOfMonth(@Param("brandId") Long brandId);

    // get revenue brand by brand id of year
    @Query("select sum(od.quantity * i.costPrice) from Order o " +
            "join o.orderDetails od " +
            "join od.instrument i " +
            "join i.brand b " +
            "where b.id = :brandId AND FUNCTION('YEAR', o.orderDate) = FUNCTION('YEAR', CURRENT_DATE)")
    Double getRevenueByBrandIdOfYear(@Param("brandId") Long brandId);
}

