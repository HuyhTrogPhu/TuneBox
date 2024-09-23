package org.example.library.service;

import org.example.library.dto.InstrumentDTO;

import java.util.List;

public interface InstrumentService {
    List<InstrumentDTO> getAllInstruments();
    InstrumentDTO getInstrumentById(Long id);
    InstrumentDTO createInstrument(InstrumentDTO instrumentDTO);
    InstrumentDTO updateInstrument(Long id, InstrumentDTO instrumentDTO);
    void deleteInstrument(Long id);
}
