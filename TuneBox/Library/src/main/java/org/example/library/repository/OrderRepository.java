package org.example.library.repository;

import org.example.library.dto.OrderDetailInfoDto;
import org.example.library.dto.OrderItemsDto;
import org.example.library.dto.OrderListDto;
import org.example.library.dto.UserIsInvoice;
import org.example.library.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // get order by user id
    @Query("select new org.example.library.dto.UserIsInvoice(o.id ,o.orderDate, o.deliveryDate, o.tax, o.totalPrice," +
            "o.totalItems, o.paymentMethod, o.status, o.shippingMethod)" +
            "from User u join u.orderList o where u.id = :userId")
    List<UserIsInvoice> findByUserId(Long userId);

    // get all order list
    @Query("select new org.example.library.dto.OrderListDto(o.id, o.orderDate, o.deliveryDate, o.tax, o.totalPrice," +
            "o.totalItems, o.paymentMethod, o.status, o.shippingMethod, o.paymentStatus )" +
            "from Order o")
    List<OrderListDto> getAllOrderList();

    // Truy vấn thông tin chung của đơn hàng (không bao gồm sản phẩm)
    @Query("SELECT new org.example.library.dto.OrderDetailInfoDto(" +
            "o.id, o.orderDate, o.deliveryDate, o.tax, o.totalPrice, o.totalItems, " +
            "o.paymentMethod, o.status, o.address, o.phoneNumber, o.shippingMethod, " +
            "o.paymentStatus, u.id, u.userName, ui.name, u.email, ui.phoneNumber, ui.location) " +
            "FROM Order o " +
            "JOIN o.user u " +
            "JOIN u.userInformation ui " +
            "WHERE o.id = :orderId")
    OrderDetailInfoDto findOrderDetailByOrderId(@Param("orderId") Long orderId);

    // Truy vấn chi tiết sản phẩm của đơn hàng
    @Query("SELECT new org.example.library.dto.OrderItemsDto(" +
            "od.id, od.quantity, ins.id, ins.name, ins.image, ins.costPrice, " +
            "cat.id, brand.id) " +
            "FROM OrderDetail od " +
            "JOIN od.instrument ins " +
            "JOIN ins.categoryIns cat " +
            "JOIN ins.brand brand " +
            "WHERE od.order.id = :orderId")
    List<OrderItemsDto> findOrderItemsByOrderId(@Param("orderId") Long orderId);


    // revenue of day
    @Query("SELECT SUM(o.totalPrice) FROM Order o WHERE o.orderDate = CURRENT_DATE")
    Double getRevenueOfDay();

    // Revenue of before day
    @Query(value = "SELECT SUM(o.total_price) FROM orders o WHERE o.order_date = DATE_SUB(CURDATE(), INTERVAL 1 DAY)", nativeQuery = true)
    Double getRevenueOfBeforeDay();


    // Revenue of week
    @Query("SELECT SUM(o.totalPrice) FROM Order o WHERE FUNCTION('WEEK', o.orderDate) " +
            "= FUNCTION('WEEK', CURRENT_DATE) AND FUNCTION('YEAR', o.orderDate) = FUNCTION('YEAR', CURRENT_DATE)")
    Double getRevenueOfWeek();

    // revenue of before week
    @Query(value = "SELECT SUM(o.total_price) FROM orders o WHERE WEEK(o.order_date) = WEEK(DATE_SUB(CURDATE(), INTERVAL 1 WEEK)) " +
            "AND YEAR(o.order_date) = YEAR(DATE_SUB(CURDATE(), INTERVAL 1 WEEK))", nativeQuery = true)
    Double getRevenueOfBeforeWeek();


    // Revenue of month
    @Query("SELECT SUM(o.totalPrice) FROM Order o WHERE FUNCTION('MONTH', o.orderDate) " +
            "= FUNCTION('MONTH', CURRENT_DATE) AND FUNCTION('YEAR', o.orderDate) = FUNCTION('YEAR', CURRENT_DATE)")
    Double getRevenueOfMonth();


    // revenue of before month
    @Query(value = "SELECT SUM(o.total_price) FROM orders o " +
            "WHERE MONTH(o.order_date) = MONTH(DATE_SUB(CURDATE(), INTERVAL 1 MONTH)) " +
            "AND YEAR(o.order_date) = YEAR(DATE_SUB(CURDATE(), INTERVAL 1 MONTH))", nativeQuery = true)
    Double getRevenueOfBeforeMonth();


    // Revenue of year
    @Query("SELECT SUM(o.totalPrice) FROM Order o WHERE FUNCTION('YEAR', o.orderDate) = FUNCTION('YEAR', CURRENT_DATE)")
    Double getRevenueOfYear();

    // revenue of before year
    @Query(value = "SELECT SUM(o.total_price) FROM orders o " +
            "WHERE YEAR(o.order_date) = YEAR(DATE_SUB(CURDATE(), INTERVAL 1 YEAR))", nativeQuery = true)
    Double getRevenueOfBeforeYear();

    // get revenue according to day
    @Query("SELECT SUM(o.totalPrice) FROM Order o WHERE o.orderDate = :date")
    Double getRevenueByDay(@Param("date") Date date);

    // get revenue between date
    @Query("SELECT SUM(o.totalPrice) FROM Order o WHERE o.orderDate BETWEEN :startDate AND :endDate")
    Double getRevenueBetweenDate(@Param("startDate") Date startDate, @Param("endDate") Date endDate);



}
