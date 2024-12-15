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
public class UserIsInvoice {
    private Long id;
    private LocalDate orderDate;

    private LocalDate deliveryDate;

    private double tax;

    private double totalPrice;

    private double totalItem;

    private String paymentMethod;

    private String status;

    private String shippingMethod;

    private String paymentStatus;
}
