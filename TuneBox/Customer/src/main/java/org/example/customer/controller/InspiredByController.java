package org.example.customer.controller;

import org.example.library.service.InspiredByService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/inspired")
public class InspiredByController {
    @Autowired
    private InspiredByService InsSer;

    @GetMapping ("/getall")
    public ResponseEntity<?> FindAll() {
        Map<String, Object> response = new HashMap<>();

        try {
            response.put("status", true);
            response.put("message", "Succesfull");
            response.put("data",InsSer.findAll());

        } catch (Exception ex) {
            response.put("status", false);
            response.put("message", null);
            response.put("data",false);
        }
        return ResponseEntity.ok(response);
    }
}
