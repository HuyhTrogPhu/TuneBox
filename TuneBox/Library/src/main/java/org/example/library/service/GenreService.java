package org.example.library.service;



import org.example.library.model.Genre;
import org.example.library.model.Talent;

import java.util.List;

public interface GenreService {
    List<Genre> findAll();
}