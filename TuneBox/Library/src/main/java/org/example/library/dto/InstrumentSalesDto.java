package org.example.library.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InstrumentSalesDto {
    
    private Long instrumentId;

    private String instrumentName;

    // để chứa tổng số lượng instrument đã bán
    private Long totalSold;
}
