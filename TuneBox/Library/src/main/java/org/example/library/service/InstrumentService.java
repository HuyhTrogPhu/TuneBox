package org.example.library.service;

import org.example.library.dto.InstrumentDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface InstrumentService {


    InstrumentDto createInstrument(InstrumentDto instrumentDto, MultipartFile image);

    List<InstrumentDto> getAllInstrument();

    InstrumentDto getInstrumentById(Long id);

    InstrumentDto updateInstrument(Long id, InstrumentDto instrumentDto, MultipartFile image);

    void deleteInstrument(Long id);

    List<InstrumentDto> getInstrumentsByBrandId(Long brandId);

    List<InstrumentDto> getInstrumentByCategoryId(Long categoryId);

    //    Get list instrument by category id anh brand id
    List<InstrumentDto> getInstrumentByCategoryIdAndBrandId(Long categoryId, Long brandId);
}

