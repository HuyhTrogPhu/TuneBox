package org.example.library.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.library.model.OrderDetail;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {

    private Long orderId;

    private LocalDate orderDate = LocalDate.now();

    private LocalDate deliveryDate = LocalDate.now();

    private double tax;

    private double totalPrice;

    private  int totalItem;

    private String paymentMethod;

    private String status;

    private Long userId;

    private List<OrderDetailDto> orderDetails;
}
