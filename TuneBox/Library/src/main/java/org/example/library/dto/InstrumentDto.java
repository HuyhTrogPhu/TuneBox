package org.example.library.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.library.model.Brand;
import org.example.library.model.CategoryIns;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InstrumentDto {

    private Long id;

    private String name;

    private double costPrice;

    private int quantity;

    private String color;

    private boolean status;

    private String image;

    private String description;

    private CategoryIns categoryIns;

    private Brand brand;
}
