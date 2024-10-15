package org.example.library.service.implement;


import lombok.AllArgsConstructor;
import org.example.library.model.InspiredBy;
import org.example.library.model.Talent;
import org.example.library.repository.InspiredByRepository;
import org.example.library.repository.TalentRepository;
import org.example.library.service.InspiredByService;
import org.example.library.service.TalentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class InpiredByServiceImpl implements InspiredByService {


    @Autowired
    private InspiredByRepository inheritedByRepository;


    @Override
    public List<InspiredBy> findAll() {
        return inheritedByRepository.findAll();
    }
}
