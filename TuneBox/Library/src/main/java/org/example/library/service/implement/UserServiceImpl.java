package org.example.library.service.implement;


import lombok.AllArgsConstructor;
import org.example.library.config.CryptePassword;
import org.example.library.dto.UserDto;
import org.example.library.mapper.UserMapper;
import org.example.library.model.User;
import org.example.library.repository.*;
import org.example.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository Repo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private InspiredByRepository inspiredByRepository;


    @Autowired
    private TalentRepository TalentRepo;

    @Autowired
    private GenreRepository GenreRepo;

    @Autowired
    private PasswordEncoder crypt;

    @Override
    public UserDto Register(UserDto userdto, Long inspiredById,Long TalentId,Long GenreId) {
//        User savedUser = new User();
//
//        savedUser.setEmail(userdto.getEmail());
//        savedUser.setPassword(crypt.encode(userdto.getPassword()));
//        savedUser.setUserName(userdto.getUserName());
//
//        savedUser.setInspiredBy(inspiredByRepository.findById(inspiredById));
//        savedUser.setTalent(userdto.getTalent());
//        savedUser.setGenre(userdto.getGenre());
//
//        savedUser.setRole(roleRepo.findByName("CUSTOMER"));
//
//
//        Repo.save(savedUser);
//        return UserMapper.maptoUserDto(savedUser);
        return null;
   }
}
