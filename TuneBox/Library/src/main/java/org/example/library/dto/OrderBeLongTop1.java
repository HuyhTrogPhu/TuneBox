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
public class OrderBeLongTop1 {

    private Long userId;

    private Long orderId;

    private LocalDate orderDate;

    private LocalDate deliveryDate;

    private int totalItems;

    private double totalPrice;

    private String paymentMethod;

    private String status;


}
