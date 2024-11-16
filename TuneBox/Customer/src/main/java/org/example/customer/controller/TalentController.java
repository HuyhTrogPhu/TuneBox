package org.example.customer.controller;


import org.example.library.service.TalentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/talent")
public class TalentController {
    @Autowired
    private TalentService TalentSer;


    @GetMapping ("/getall")
    public ResponseEntity<?> FindAll() {
        Map<String, Object> response = new HashMap<>();

        try {
            response.put("status", true);
            response.put("message", "Succesfull");
            response.put("data",TalentSer.findAll());

        } catch (Exception ex) {
            response.put("status", false);
            response.put("message", null);
            response.put("data",false);
        }
        return ResponseEntity.ok(response);
    }
}
