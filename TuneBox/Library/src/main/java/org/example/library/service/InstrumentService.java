package org.example.library.service;

import org.example.library.dto.InstrumentDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface InstrumentService {
    InstrumentDto createInstrument(InstrumentDto instrumentDto, MultipartFile[] images);

    List<InstrumentDto> getAllInstrument();

    InstrumentDto getInstrumentById(Long id);

    InstrumentDto updateInstrument(Long id, InstrumentDto instrumentDto, MultipartFile[] images);

    void deleteInstrument(Long id);

    List<InstrumentDto> getInstrumentsByBrandId(Long brandId);

}

