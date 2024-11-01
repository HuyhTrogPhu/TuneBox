package org.example.library.service;

import jakarta.servlet.http.HttpServletRequest;
import org.example.library.dto.*;
import org.example.library.model.Order;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface OrderService {

    List<OrderListDto> getOrderList();

    List<UserIsInvoice> getOrderByUserId(Long userId);

    Order getOrderById(Long orderId);

    OrderDto createOrder(OrderDto orderDto, Long userId);

    OrderDetailInfoDto getOrderDetailByOrderId(Long orderId);

    void updateOrderStatus(Long orderId, String status, LocalDate deliveryDate, String paymentStatus );


    Double revenueOfDay();

    Double revenueOfWeek();

    Double revenueOfMonth();

    Double revenueOfYear();

    Double revenueBeforeOfDay();

    Double revenueBeforeOfWeek();

    Double revenueBeforeOfMonth();

    Double revenueBeforeOfYear();

    // revenue by day
    Double revenueByDay(LocalDate date);

    // revenue between days
    Double revenueBetweenDate(LocalDate startDate, LocalDate endDate);

    // revenue by week
    Double revenueByWeek(LocalDate date);

    // revenue between weeks
    Double revenueBetweenWeeks(LocalDate startDate, LocalDate endDate);

    // revenue by month
    Double revenueByMonth(int year, int month);

    // revenue between months
    Double revenueBetweenMonths(int year, int startMonth, int endMonth);

    // revenue by year
    Double revenueByYear(int year);

    // revenue between years
    Double revenueBetweenYears(int startYear, int endYear);



}
