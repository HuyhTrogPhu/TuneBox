package org.example.ecommerceadmin.controller;

import lombok.AllArgsConstructor;

import org.example.library.dto.InstrumentDto;
import org.example.library.model.Instrument;
import org.example.library.repository.InstrumentRepository;
import org.example.library.service.implement.InstrumentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin("*")
@RestController
@AllArgsConstructor
@RequestMapping("/e-comAdmin/instrument")
public class InstrumentController {

    @Autowired
    private InstrumentServiceImpl instrumentService;


    //    Add new instrument
    @PostMapping
    public ResponseEntity<InstrumentDto> createInstrument(@RequestBody InstrumentDto instrumentDto,
                                                          @RequestParam("insImage") MultipartFile image) {
        InstrumentDto saveInstrument = instrumentService.createInstrument(instrumentDto, image);
        return new ResponseEntity<>(saveInstrument, HttpStatus.CREATED);
    }

    //    Get all instrument
    @GetMapping("/getAllInstrument")
    public ResponseEntity<List<InstrumentDto>> getAll() {
        List<InstrumentDto> instruments = instrumentService.getAllInstrument();
        return ResponseEntity.ok(instruments);
    }


    //    Get instrument by id
    @GetMapping("{instrumentId}")
    public ResponseEntity<InstrumentDto> getInstrumentById(@PathVariable("instrumentId") Long id) {
        InstrumentDto instrumentDto = instrumentService.getInstrumentById(id);
        return ResponseEntity.ok(instrumentDto);
    }


    //    Update Instrument
    @PutMapping("{instrumentId}")
    public ResponseEntity<InstrumentDto> updateInstrument(@PathVariable("instrumentId") Long id,
                                                          @RequestBody InstrumentDto instrumentDto,
                                                          @RequestParam("insImage") MultipartFile image) {
        InstrumentDto saveInstrument = instrumentService.updateInstrument(id, instrumentDto, image);
        return ResponseEntity.ok(saveInstrument);
    }

    //    Delete instrument
    @DeleteMapping("{instrumentId}")
    public ResponseEntity<String> deleteInstrument(@PathVariable("instrumentId") Long id) {
        instrumentService.deleteInstrument(id);
        return ResponseEntity.ok("Delete instrument successfully");
    }
}
