package org.example.library.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.library.model.Instrument;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDto {

    private Long instrumentId; // Chỉ lưu ID của nhạc cụ
    private String name;       // Tên nhạc cụ
    private String image;      // URL hình ảnh nhạc cụ
    private Double costPrice;  // Giá của nhạc cụ
    private Integer quantity;  // Số lượng
}
