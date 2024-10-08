package org.example.library.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BrandsDto {


    private Long id;

    private String name;

    private String brandImage;

    private String description;

    private boolean status;

    public boolean getStatus() {
        return status;
    }
    public boolean isStatus() {
        return status;
    }
}
