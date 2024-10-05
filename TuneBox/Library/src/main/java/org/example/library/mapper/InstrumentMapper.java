package org.example.library.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.library.dto.InstrumentDto;
import org.example.library.model.Instrument;

import java.util.List;

public class InstrumentMapper {

    private static final ObjectMapper objectMapper = new ObjectMapper(); // Tạo ObjectMapper cho việc chuyển đổi

    // Chuyển từ thực thể Instrument sang DTO
    public static InstrumentDto mapperInstrumentDto(Instrument instrument) {
        try {
            // Chuyển chuỗi JSON thành List<String>
            List<String> imageList = objectMapper.readValue(instrument.getImage(), new TypeReference<List<String>>() {});
            return new InstrumentDto(
                    instrument.getId(),
                    instrument.getName(),
                    instrument.getCostPrice(),
                    instrument.getQuantity(),
                    instrument.getColor(),
                    instrument.isStatus(),
                    imageList,  // Ánh xạ List<String>
                    instrument.getDescription(),
                    instrument.getCategoryIns(),
                    instrument.getBrand()
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Chuyển từ DTO sang thực thể Instrument
    public static Instrument mapperInstrument(InstrumentDto instrumentDto) {
        try {
            // Chuyển List<String> thành chuỗi JSON
            String imageJson = objectMapper.writeValueAsString(instrumentDto.getImage());
            return new Instrument(
                    instrumentDto.getId(),
                    instrumentDto.getName(),
                    instrumentDto.getCostPrice(),
                    instrumentDto.getQuantity(),
                    instrumentDto.getColor(),
                    instrumentDto.isStatus(),
                    imageJson, // Lưu chuỗi JSON
                    instrumentDto.getDescription(),
                    instrumentDto.getCategoryIns(),
                    instrumentDto.getBrand()
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
