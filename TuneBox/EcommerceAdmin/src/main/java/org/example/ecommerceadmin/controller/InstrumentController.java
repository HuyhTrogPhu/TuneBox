package org.example.ecommerceadmin.controller;

import lombok.AllArgsConstructor;

import org.example.library.dto.BrandsDto;
import org.example.library.dto.CategoryDto;
import org.example.library.dto.InstrumentDto;
import org.example.library.model.CategoryIns;
import org.example.library.model.Instrument;
import org.example.library.repository.InstrumentRepository;
import org.example.library.service.implement.BrandServiceImpl;
import org.example.library.service.implement.CategoryServiceImpl;
import org.example.library.service.implement.InstrumentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
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

    @Autowired
    private BrandServiceImpl brandService;

    @Autowired
    private CategoryServiceImpl categoryService;


    //    Add new instrument
    @PostMapping
    public ResponseEntity<InstrumentDto> createInstrument(@RequestBody InstrumentDto instrumentDto,
                                                          @RequestParam("insImage") MultipartFile image) {
        InstrumentDto saveInstrument = instrumentService.createInstrument(instrumentDto, image);
        return new ResponseEntity<>(saveInstrument, HttpStatus.CREATED);
    }

    //    Get all instrument
    @GetMapping
    public ResponseEntity<List<InstrumentDto>> getAll() {
        List<InstrumentDto> instruments = instrumentService.getAllInstrument();
        return ResponseEntity.ok(instruments);
    }

    //    Get all brand
    @GetMapping
    public ResponseEntity<List<BrandsDto>> getAllBrand() {
        List<BrandsDto> brandsDto = brandService.getAllBrand();
        return ResponseEntity.ok(brandsDto);
    }

    //    Get all category
    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategory() {
        List<CategoryDto> categoryDto = categoryService.getAllCategory();
        return ResponseEntity.ok(categoryDto);
    }


    //    Get instrument by id
    @GetMapping("{id}")
    public ResponseEntity<InstrumentDto> getInstrumentById(@PathVariable Long id) {
        InstrumentDto instrumentDto = instrumentService.getInstrumentById(id);
        return ResponseEntity.ok(instrumentDto);
    }


    //    Update Instrument
    @PutMapping("{id}")
    public ResponseEntity<InstrumentDto> updateInstrument(@PathVariable Long id,
                                                          @RequestBody InstrumentDto instrumentDto,
                                                          @RequestParam("insImage") MultipartFile image) {
        InstrumentDto saveInstrument = instrumentService.updateInstrument(id, instrumentDto, image);
        return ResponseEntity.ok(saveInstrument);
    }

    //    Delete instrument
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteInstrument(@PathVariable Long id) {
        instrumentService.deleteInstrument(id);
        return ResponseEntity.ok("Delete instrument successfully");
    }
}
