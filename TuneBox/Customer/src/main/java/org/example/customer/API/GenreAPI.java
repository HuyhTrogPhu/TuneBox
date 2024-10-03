package org.example.customer.API;

import org.example.library.model.Genre;
import org.example.library.model.RespondModel;
import org.example.library.service.GenreService;
import org.example.library.service.TalentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/Genre")
public class GenreAPI {
    @Autowired
    private GenreService GenreService;

    @GetMapping ("/getall")
    public ResponseEntity<?> FindAll() {
        RespondModel response = new RespondModel();
        try {
            response.setStatus(true);
            response.setMessage("succesfull");
            response.setData(GenreService.findAll());

        } catch (Exception ex) {
            response.setMessage(ex.getMessage());
            response.setData(null);
            response.setStatus(false);
        }
        return ResponseEntity.ok(response);
    }
}
