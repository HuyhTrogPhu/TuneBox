package org.example.library.service;

import org.example.library.dto.InstrumentDto;
import org.example.library.dto.InstrumentSalesDto;
import org.example.library.dto.StatisticalInstrumentDto;
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

    // get id and name instrument
    List<StatisticalInstrumentDto> getIdAndNameInstrument();

    //    Get list instrument by category id anh brand id
    List<InstrumentDto> getInstrumentByCategoryIdAndBrandId(Long categoryId, Long brandId);

    // get instrument sale the most of day
    InstrumentSalesDto instrumentSalesTheMostOfDay();

    // get instrument sale the most of week
    InstrumentSalesDto instrumentSalesTheMostOfWeek();

    // get instrument sale the most of month
    InstrumentSalesDto instrumentSalesTheMostOfMonth();

    // get instrument sale the least of day
    InstrumentSalesDto instrumentSalesTheLeastOfDay();

    // get instrument sale the least of week
    InstrumentSalesDto instrumentSalesTheLeastOfWeek();

    // get instrument sale the least of month
    InstrumentSalesDto instrumentSalesTheLeastOfMonth();

}

