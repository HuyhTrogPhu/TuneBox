package org.example.library.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.library.model.Brand;
import org.example.library.model.CategoryIns;

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
    @Override
    public String toString() {
        return "InstrumentDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", costPrice=" + costPrice +
                ", quantity=" + quantity +
                ", color='" + color + '\'' +
                ", status=" + status +
                ", description='" + description + '\'' +
                ", categoryIns=" + (categoryIns != null ? categoryIns.getName() : "null") +  // Hiển thị tên category
                ", brand=" + (brand != null ? brand.getName() : "null") +  // Hiển thị tên brand
                '}';
    }
}
