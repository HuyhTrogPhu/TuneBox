package org.example.library.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailInfoDto {
    private Long id;
    private LocalDate orderDate;
    private LocalDate deliveryDate;
    private double tax;
    private double totalPrice;
    private int totalItems;
    private String paymentMethod;
    private String status;
    private String address;
    private String phoneNumber;
    private String shippingMethod;
    private String paymentStatus;

    // user information
    private Long userId;
    private String userName;
    private String name;
    private String email;
    private String phone;
    private String location;

    // Danh sách các sản phẩm trong đơn hàng
    private List<OrderItemsDto> orderItems;

    public OrderDetailInfoDto(Long id, LocalDate orderDate,
                              LocalDate deliveryDate, double tax,
                              double totalPrice, int totalItems,
                              String paymentMethod, String status,
                              String address, String phoneNumber,
                              String shippingMethod, String paymentStatus,
                              Long userId, String userName, String name,
                              String email, String phone, String location) {
        this.id = id;
        this.orderDate = orderDate;
        this.deliveryDate = deliveryDate;
        this.tax = tax;
        this.totalPrice = totalPrice;
        this.totalItems = totalItems;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.shippingMethod = shippingMethod;
        this.paymentStatus = paymentStatus;
        this.userId = userId;
        this.userName = userName;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.location = location;
    }
}

