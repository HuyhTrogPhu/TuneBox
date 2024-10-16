package org.example.library.model;



import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_id")
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
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order")

    private List<OrderDetail> orderDetails;


}
