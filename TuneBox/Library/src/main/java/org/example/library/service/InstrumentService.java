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

    // Get list instrument by category id anh brand id
    List<InstrumentDto> getInstrumentByCategoryIdAndBrandId(Long categoryId, Long brandId);

    // get instrument sale the most of day
    List<InstrumentSalesDto> instrumentSalesTheMostOfDay();

    // get instrument sale the most of week
    List<InstrumentSalesDto> instrumentSalesTheMostOfWeek();

    // get instrument sale the most of month
    List<InstrumentSalesDto> instrumentSalesTheMostOfMonth();

    // get instrument sale the least of day
    List<InstrumentSalesDto> instrumentSalesTheLeastOfDay();

    // get instrument sale the least of week
    List<InstrumentSalesDto> instrumentSalesTheLeastOfWeek();

    // get instrument sale the least of month
    List<InstrumentSalesDto> instrumentSalesTheLeastOfMonth();

    // get revenue instrument of day by instrumentId
    Double getRevenueInstrumentOfDay(Long instrumentId);

    // get revenue instrument of week by instrumentId
    Double getRevenueInstrumentOfWeek(Long instrumentId);

    // get revenue instrument of month by instrumentId
    Double getRevenueInstrumentOfMonth(Long instrumentId);

    // get revenue instrument of year by instrumentId
    Double getRevenueInstrumentOfYear(Long instrumentId);

    Integer getInstrumentQuantityById(Long id);
}

