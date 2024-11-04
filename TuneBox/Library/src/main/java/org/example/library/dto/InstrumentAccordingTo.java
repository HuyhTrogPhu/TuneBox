package org.example.library.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InstrumentAccordingTo {

    private Long instrumentId;

    private String instrumentName;

    private double costPrice;

    private String image;

    // để chứa tổng số lượng instrument đã bán
    private Long totalSold;


}
