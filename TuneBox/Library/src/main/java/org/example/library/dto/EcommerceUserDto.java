package org.example.library.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EcommerceUserDto {

    private Long id;

    private String userName;

    private String email;

//    private String location;

    private Long totalOrderCount;

    private double totalOrderAmount;

}
