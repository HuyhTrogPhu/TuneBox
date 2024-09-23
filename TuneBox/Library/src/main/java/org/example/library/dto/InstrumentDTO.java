package org.example.library.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InstrumentDTO {
    private Long id;
    private String name;
    private double costPrice;
    private int quantity;
    private String color;
    private String image;
    private String description;
    private Long categoryInsId;
    private Long brandId;
}
