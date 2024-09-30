package org.example.customer.API;

import org.example.library.model.RespondModel;
import org.example.library.service.InspiredByService;
import org.example.library.service.TalentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin("*")
@RestController
@RequestMapping("/inspired")
public class InspiredByAPI {
    @Autowired
    private InspiredByService InsSer;

    @GetMapping ("/getall")
    public ResponseEntity<?> FindAll() {
        RespondModel response = new RespondModel();
        try {
            response.setStatus(true);
            response.setMessage("succesfull");
            response.setData(InsSer.findAll());

        } catch (Exception ex) {
            response.setMessage(ex.getMessage());
            response.setData(null);
            response.setStatus(false);
        }
        return ResponseEntity.ok(response);
    }
}
