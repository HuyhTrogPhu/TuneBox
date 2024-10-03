package org.example.library.mapper;

import org.example.library.dto.InstrumentDto;
import org.example.library.model.Instrument;
import org.example.library.model.InstrumentImage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InstrumentMapper {

    public static InstrumentDto mapperInstrumentDto(Instrument instrument) {
        // Ánh xạ danh sách hình ảnh trực tiếp
        List<InstrumentImage> images = instrument.getImages().stream()
                .map(image -> {
                    // Bạn có thể chỉ cần trả về hình ảnh mà không cần tạo mới
                    return new InstrumentImage(image.getId(), image.getImagePath(), null); // Giữ instrument là null
                }).collect(Collectors.toList());

        return new InstrumentDto(instrument.getId(), instrument.getName(), instrument.getCostPrice(),
                instrument.getQuantity(), instrument.getColor(), instrument.isStatus(), images,
                instrument.getDescription(), instrument.getCategoryIns(), instrument.getBrand());
    }

    public static Instrument mapperInstrument(InstrumentDto instrumentDto) {
        List<InstrumentImage> images = (instrumentDto.getImage() != null) ? instrumentDto.getImage().stream()
                .map(image -> new InstrumentImage(image.getId(), image.getImagePath(), null)) // Tạo đối tượng InstrumentImage
                .collect(Collectors.toList()) : new ArrayList<>(); // Trả về danh sách rỗng nếu null

        return new Instrument(instrumentDto.getId(), instrumentDto.getName(), instrumentDto.getCostPrice(),
                instrumentDto.getQuantity(), instrumentDto.getColor(), instrumentDto.isStatus(), images,
                instrumentDto.getDescription(), instrumentDto.getCategoryIns(), instrumentDto.getBrand());
    }
}