package org.example.library.repository;

import org.example.library.dto.CategorySalesDto;
import org.example.library.dto.StatisticalCategoryDto;
import org.example.library.model.CategoryIns;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryInsRepository extends JpaRepository<CategoryIns, Long> {

    //list id and name cate

    @Query("select new org.example.library.dto.StatisticalCategoryDto(i.id, i.name) from CategoryIns i ")
    List<StatisticalCategoryDto> getStatisticalCategory();

    // bán chạy nhất trong ngày
//    @Query("SELECT new org.example.library.dto.CategorySalesDto(c.id, c.name, SUM(o.quantity)) " +
//            "FROM Order o JOIN o.orderDetails i JOIN i.cate c " +
//            "WHERE o.order.date = CURRENT_DATE " +
//            "GROUP BY c.id, c.name " +
//            "ORDER BY SUM(o.quantity) DESC")
    List<CategorySalesDto> getCategorySalesTheMostOfDay();

    // bán chạy nhất trong tuần

    List<CategorySalesDto> getCategorySalesTheMostOfWeek();

    // bán chạy nhất trong tháng

    List<CategorySalesDto> getCategorySalesTheMostOfMonth();


    // bán ít nhất trong ngày

    List<CategorySalesDto> getCategorySalesTheLeastOfDay();

    // bán ít nhất trong tuần

    List<CategorySalesDto> getCategorySalesTheLeastOfWeek();

    // bán it nhất trong tháng

    List<CategorySalesDto> getCategorySalesTheLeastOfMonth();


    //Doanh thu cua category theo ngay hiện tại

    Double getTotalRevenueCategoryOfDay(@Param("categoryId") Long categoryId);

    // doanh thu của category theo tuần hien tai


    Double getTotalRevenueCategoryOfWeek(@Param("categoryId") Long categoryId);

    // doanh thu của category theo tháng hien tai

    Double getTotalRevenueCategoryOfMonth(@Param("categoryId") Long categoryId);

    // doanh thu của category theo năm hien tai


    Double getTotalRevenueCategoryOfYear(@Param("categoryId") Long categoryId);


}
