package org.example.library.service.implement;

import org.example.library.dto.InstrumentDTO;
import org.example.library.mapper.InstrumentMapper;
import org.example.library.model.Instrument;
import org.example.library.repository.InstrumentRepository;
import org.example.library.service.InstrumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InstrumentServiceImpl implements InstrumentService {
    @Autowired
    private InstrumentRepository instrumentRepository;

    @Autowired
    private InstrumentMapper instrumentMapper;

    @Override
    public List<InstrumentDTO> getAllInstruments() {
        return instrumentRepository.findAll().stream()
                .map(instrumentMapper::toDTO)
                .collect(Collectors.toList());
    }
    @Override
    public InstrumentDTO getInstrumentById(Long id) {
        return instrumentRepository.findById(id)
                .map(instrumentMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Instrument not found"));
    }

    @Override
    public InstrumentDTO createInstrument(InstrumentDTO instrumentDTO) {
        Instrument instrument = instrumentMapper.toEntity(instrumentDTO);
        Instrument savedInstrument = instrumentRepository.save(instrument);
        return instrumentMapper.toDTO(savedInstrument);
    }

    @Override
    public InstrumentDTO updateInstrument(Long id, InstrumentDTO instrumentDTO) {
        Instrument existingInstrument = instrumentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Instrument not found"));

        existingInstrument.setName(instrumentDTO.getName());
        existingInstrument.setCostPrice(instrumentDTO.getCostPrice());
        existingInstrument.setQuantity(instrumentDTO.getQuantity());
        existingInstrument.setColor(instrumentDTO.getColor());
        existingInstrument.setImage(instrumentDTO.getImage());
        existingInstrument.setDescription(instrumentDTO.getDescription());

        Instrument updatedInstrument = instrumentRepository.save(existingInstrument);
        return instrumentMapper.toDTO(updatedInstrument);
    }

    @Override
    public void deleteInstrument(Long id) {
        instrumentRepository.deleteById(id);
    }
}
