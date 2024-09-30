package org.example.library.service;



import org.example.library.dto.RequestSignUpModel;
import org.example.library.dto.UserDto;
import org.example.library.model.Talent;
import org.example.library.model.User;

import java.util.List;
import java.util.Optional;

public interface TalentService {
    List<Talent> findAll();
}
