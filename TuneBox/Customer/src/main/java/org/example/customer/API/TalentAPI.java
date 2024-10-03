package org.example.customer.API;


import org.example.library.model.RespondModel;

import org.example.library.service.TalentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;




@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/talent")
public class TalentAPI {
    @Autowired
    private TalentService TalentSer;

    @GetMapping ("/getall")
    public ResponseEntity<?> FindAll() {
        RespondModel response = new RespondModel();
        try {
            response.setStatus(true);
            response.setMessage("succesfull");
            response.setData(TalentSer.findAll());

        } catch (Exception ex) {
            response.setMessage(ex.getMessage());
            response.setData(null);
            response.setStatus(false);
        }
        return ResponseEntity.ok(response);
    }
}
