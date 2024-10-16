package org.example.library.dto;


import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.library.model.Instrument;
import org.example.library.model.Order;
import org.example.library.model.OrderDetail;
import org.example.library.model.User;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDto {

    private Long id;

    private int quantity;

    private Order order;

    private Instrument instrument;
}
