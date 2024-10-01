package org.example.library.service.implement;


import lombok.AllArgsConstructor;
import org.example.library.model.Genre;
import org.example.library.model.Talent;
import org.example.library.repository.GenreRepository;

import org.example.library.service.GenreService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GenreServiceImpl implements GenreService {


    @Autowired
    private GenreRepository GenRepo;


    @Override
    public List<Genre> findAll() {
        return GenRepo.findAll();
    }
}
