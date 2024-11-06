package org.example.library.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StatisticalOrder {

    private Long userId;

    private String name;

    private String email;

    private Long oderId;

    private LocalDate orderDate;

    private LocalDate deliveryDate;

    private String phone;

    private double totalPrice;

    private int totalItems;

    private String paymentMethod;

    private String status;

    private String shippingMethod;

    private String paymentStatus;

}
