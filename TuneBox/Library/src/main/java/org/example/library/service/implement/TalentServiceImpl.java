package org.example.library.service.implement;


import lombok.AllArgsConstructor;
import org.example.library.dto.RequestSignUpModel;
import org.example.library.dto.UserDto;
import org.example.library.mapper.UserMapper;
import org.example.library.model.Genre;
import org.example.library.model.InspiredBy;
import org.example.library.model.Talent;
import org.example.library.model.User;
import org.example.library.repository.*;
import org.example.library.service.TalentService;
import org.example.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class TalentServiceImpl implements TalentService {


    @Autowired
    private TalentRepository TalentRepo;


    @Override
    public List<Talent> findAll() {
        return TalentRepo.findAll();
    }
}
