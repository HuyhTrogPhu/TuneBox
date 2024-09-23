package org.example.library.dto;


import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.library.model.OrderDetail;
import org.example.library.model.Street;
import org.example.library.model.User;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {

    private Long id;

    private Date orderDate;

    private Date deliveryDate;

    private double tax;

    private double totalPrice;

    private int totalItems;

    private String paymentMethod;

    private String status;

    private User user;

    private List<OrderDetail> orderDetails;
}
