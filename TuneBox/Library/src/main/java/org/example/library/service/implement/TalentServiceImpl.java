package org.example.library.service.implement;


import lombok.AllArgsConstructor;

import org.example.library.model.Talent;
import org.example.library.repository.*;
import org.example.library.service.TalentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TalentServiceImpl implements TalentService {


    @Autowired
    private TalentRepository talentRepository;


    @Override
    public List<Talent> findAll() {
        return talentRepository.findAll();
    }
}
