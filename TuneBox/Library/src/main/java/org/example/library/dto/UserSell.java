package org.example.library.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSell {

    private Long userId;

    private String name;

    private String phoneNumber;

    private String userName;

    private String location;

    private String email;

    private Long totalOrder;

    private double sumTotalPrice;

}
