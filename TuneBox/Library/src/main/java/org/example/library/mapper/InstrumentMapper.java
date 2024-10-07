package org.example.library.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.library.dto.InstrumentDto;
import org.example.library.model.Instrument;

import java.util.List;

public class InstrumentMapper {

    public static InstrumentDto mapperInstrumentDto(Instrument instrument) {
        return new InstrumentDto(
                instrument.getId(),
                instrument.getName(),
                instrument.getCostPrice(),
                instrument.getQuantity(),
                instrument.getColor(),
                instrument.isStatus(),
                instrument.getImage(),
                instrument.getDescription(),
                instrument.getCategoryIns(),
                instrument.getBrand()
        );
    }

    public static Instrument mapperInstrument(InstrumentDto instrumentDto) {
        return new Instrument(
                instrumentDto.getId(),
                instrumentDto.getName(),
                instrumentDto.getCostPrice(),
                instrumentDto.getQuantity(),
                instrumentDto.getColor(),
                instrumentDto.isStatus(),
                instrumentDto.getImage(),
                instrumentDto.getDescription(),
                instrumentDto.getCategoryIns(),
                instrumentDto.getBrand()
        );
    }
}
