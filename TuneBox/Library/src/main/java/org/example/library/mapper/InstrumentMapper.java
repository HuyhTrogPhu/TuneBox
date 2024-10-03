package org.example.library.mapper;

import org.example.library.dto.InstrumentDto;
import org.example.library.model.Instrument;

public class InstrumentMapper {
    public static InstrumentDto mapperInstrumentDto(Instrument instrument) {
        return new InstrumentDto(instrument.getId(), instrument.getName(), instrument.getCostPrice(),
                instrument.getQuantity(), instrument.getColor(), instrument.isStatus(), instrument.getImages(),
                instrument.getDescription(), instrument.getCategoryIns(), instrument.getBrand());
    }

    public static Instrument mapperInstrument(InstrumentDto instrumentDto) {
        return new Instrument(instrumentDto.getId(), instrumentDto.getName(), instrumentDto.getCostPrice(), instrumentDto.getQuantity()
                , instrumentDto.getColor(), instrumentDto.isStatus(), instrumentDto.getImage(), instrumentDto.getDescription()
                , instrumentDto.getCategoryIns(), instrumentDto.getBrand());
    }
}