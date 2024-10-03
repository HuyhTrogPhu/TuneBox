package org.example.customer.controller;


import lombok.AllArgsConstructor;
import org.example.library.dto.BrandsDto;
import org.example.library.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@AllArgsConstructor
@RequestMapping("/shop/brandPage")
public class BrandController {

    @Autowired
    private BrandService brandService;


}
