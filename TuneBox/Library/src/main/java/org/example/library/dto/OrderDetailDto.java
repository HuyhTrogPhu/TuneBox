package org.example.library.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDto {

    private Long id;

    private int quantity;

    private Long instrumentId;
    private String instrumentName;
    private String image;
    private String costPrice;
}
