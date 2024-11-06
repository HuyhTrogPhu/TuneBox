package org.example.library.repository;

import org.example.library.dto.CategorySalesDto;
import org.example.library.dto.StatisticalCategoryDto;
import org.example.library.model.CategoryIns;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryInsRepository extends JpaRepository<CategoryIns, Long> {

    // bán chạy nhất trong ngày
    @Query("select new org.example.library.dto.CategorySalesDto(ca.id, ca.name, i.name, i.image, i.costPrice, i.quantity, sum(od.quantity))" +
            "from Order o join o.orderDetails od join od.instrument i join i.categoryIns ca " +
            "where o.orderDate = current_date " +
            "group by ca.id, ca.name, i.name, i.image, i.costPrice, i.quantity " +
            "order by sum(od.quantity) desc")
    List<CategorySalesDto> getCategorySalesTheMostDay();

    // bán chạy nhất trong tuần
    @Query("select new org.example.library.dto.CategorySalesDto(ca.id, ca.name, i.name, i.image, i.costPrice, i.quantity, sum(od.quantity))" +
            "from Order o join o.orderDetails od join od.instrument i join i.categoryIns ca " +
            "where YEAR(o.orderDate) = YEAR(CURRENT_DATE) AND WEEK(o.orderDate) = WEEK(CURRENT_DATE) " +
            "group by ca.id, ca.name, i.name, i.image, i.costPrice, i.quantity " +
            "order by sum(od.quantity) desc")
    List<CategorySalesDto> getCategorySalesTheMostWeek();

    // bán chạy nhất trong tháng
    @Query("select new org.example.library.dto.CategorySalesDto(ca.id, ca.name, i.name, i.image, i.costPrice, i.quantity, sum(od.quantity))" +
            "from Order o join o.orderDetails od join od.instrument i join i.categoryIns ca " +
            "WHERE MONTH(o.orderDate) = MONTH(CURRENT_DATE) AND YEAR(o.orderDate) = YEAR(CURRENT_DATE) " +
            "group by ca.id, ca.name, i.name, i.image, i.costPrice, i.quantity " +
            "order by sum(od.quantity) desc")
    List<CategorySalesDto> getCategorySalesTheMostMonth();

    // Bán ít nhất trong ngày
    @Query("select new org.example.library.dto.CategorySalesDto(ca.id, ca.name, i.name, i.image, i.costPrice, i.quantity, sum(od.quantity))" +
            "from Order o join o.orderDetails od join od.instrument i join i.categoryIns ca " +
            "where o.orderDate = current_date " +
            "group by ca.id, ca.name, i.name, i.image, i.costPrice, i.quantity " +
            "order by sum(od.quantity) asc")
    List<CategorySalesDto> getCategorySalesTheLeastDay();

    // Bán ít nhất trong tuần
    @Query("select new org.example.library.dto.CategorySalesDto(ca.id, ca.name, i.name, i.image, i.costPrice, i.quantity, sum(od.quantity))" +
            "from Order o join o.orderDetails od join od.instrument i join i.categoryIns ca " +
            "where YEAR(o.orderDate) = YEAR(CURRENT_DATE) AND WEEK(o.orderDate) = WEEK(CURRENT_DATE) " +
            "group by ca.id, ca.name, i.name, i.image, i.costPrice, i.quantity " +
            "order by sum(od.quantity) asc")
    List<CategorySalesDto> getCategorySalesTheLeastWeek();

    // Bán ít nhất trong tháng
    @Query("select new org.example.library.dto.CategorySalesDto(ca.id, ca.name, i.name, i.image, i.costPrice, i.quantity, sum(od.quantity))" +
            "from Order o join o.orderDetails od join od.instrument i join i.categoryIns ca " +
            "where MONTH(o.orderDate) = MONTH(CURRENT_DATE) AND YEAR(o.orderDate) = YEAR(CURRENT_DATE) " +
            "group by ca.id, ca.name, i.name, i.image, i.costPrice, i.quantity " +
            "order by sum(od.quantity) asc")
    List<CategorySalesDto> getCategorySalesTheLeastMonth();

    // get id and name of category
    @Query("select new org.example.library.dto.StatisticalCategoryDto(ca.id, ca.name) " +
            "from CategoryIns ca")
    List<StatisticalCategoryDto> getCategoryIdsAndNames();

    // get revenue category by category id of day
    @Query("select sum(od.quantity * i.costPrice) from Order o " +
            "join o.orderDetails od " +
            "join od.instrument i " +
            "join i.categoryIns ca " +
            "where ca.id = :categoryId AND FUNCTION('YEARWEEK', o.orderDate, 1) = FUNCTION('YEARWEEK', CURRENT_DATE, 1)")
    Double getRevenueByCategoryIdOfDay(@Param("categoryId") Long categoryId);

    // get revenue category by category id of week
    @Query("select sum(od.quantity * i.costPrice) from Order o " +
            "join o.orderDetails od " +
            "join od.instrument i " +
            "join i.categoryIns ca " +
            "where ca.id = :categoryId AND FUNCTION('YEARWEEK', o.orderDate, 1) = FUNCTION('YEARWEEK', CURRENT_DATE, 1)")
    Double getRevenueByCategoryIdOfWeek(@Param("categoryId") Long categoryId);

    // get revenue category by category id of month
    @Query("select sum(od.quantity * i.costPrice) from Order o " +
            "join o.orderDetails od " +
            "join od.instrument i " +
            "join i.categoryIns ca " +
            "where ca.id = :categoryId AND FUNCTION('YEAR', o.orderDate) = FUNCTION('YEAR', CURRENT_DATE) " +
            "AND FUNCTION('MONTH', o.orderDate) = FUNCTION('MONTH', CURRENT_DATE)")
    Double getRevenueByCategoryIdOfMonth(@Param("categoryId") Long categoryId);

    // get revenue category by category id of year
    @Query("select sum(od.quantity * i.costPrice) from Order o " +
            "join o.orderDetails od " +
            "join od.instrument i " +
            "join i.categoryIns ca " +
            "where ca.id = :categoryId AND FUNCTION('YEAR', o.orderDate) = FUNCTION('YEAR', CURRENT_DATE)")
    Double getRevenueByCategoryIdOfYear(@Param("categoryId") Long categoryId);
}
