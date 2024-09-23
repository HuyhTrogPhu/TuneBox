package org.example.ecommerceadmin.controller;

import lombok.AllArgsConstructor;
import org.example.library.dto.InstrumentDTO;
import org.example.library.service.InstrumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/e-comAdmin/instruments")
public class InstrumentController {
    @Autowired
    private InstrumentService instrumentService;

    // Lấy danh sách tất cả các nhạc cụ
    @GetMapping
    public ResponseEntity<List<InstrumentDTO>> getAllInstruments() {
        List<InstrumentDTO> instruments = instrumentService.getAllInstruments();
        return ResponseEntity.ok(instruments);
    }

    // Lấy thông tin một nhạc cụ theo ID
    @GetMapping("/{id}")
    public ResponseEntity<InstrumentDTO> getInstrumentById(@PathVariable Long id) {
        InstrumentDTO instrument = instrumentService.getInstrumentById(id);
        return ResponseEntity.ok(instrument);
    }

    // Tạo một nhạc cụ mới
    @PostMapping
    public ResponseEntity<InstrumentDTO> createInstrument(@RequestBody InstrumentDTO instrumentDTO) {
        InstrumentDTO createdInstrument = instrumentService.createInstrument(instrumentDTO);
        return ResponseEntity.status(201).body(createdInstrument);
    }

    // Cập nhật thông tin nhạc cụ
    @PutMapping("/{id}")
    public ResponseEntity<InstrumentDTO> updateInstrument(@PathVariable Long id, @RequestBody InstrumentDTO instrumentDTO) {
        InstrumentDTO updatedInstrument = instrumentService.updateInstrument(id, instrumentDTO);
        return ResponseEntity.ok(updatedInstrument);
    }

    // Xóa nhạc cụ theo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInstrument(@PathVariable Long id) {
        instrumentService.deleteInstrument(id);
        return ResponseEntity.noContent().build();
    }

}
