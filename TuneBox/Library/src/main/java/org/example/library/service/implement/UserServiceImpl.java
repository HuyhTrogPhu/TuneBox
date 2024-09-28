package org.example.library.service.implement;


import lombok.AllArgsConstructor;
import org.example.library.config.CryptePassword;
import org.example.library.dto.UserDto;
import org.example.library.mapper.UserMapper;
import org.example.library.model.Genre;
import org.example.library.model.InspiredBy;
import org.example.library.model.Talent;
import org.example.library.model.User;
import org.example.library.repository.*;
import org.example.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

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
    public UserDto Register(UserDto userdto, String[] inspiredByName,String[] TalentName,String[] GenreName) {
        User savedUser = new User();

        savedUser.setEmail(userdto.getEmail());
        savedUser.setPassword(crypt.encode(userdto.getPassword()));
        savedUser.setUserName(userdto.getUserName());



        List<InspiredBy> inspiredByList = new ArrayList<>();
        List<Talent> talentList = new ArrayList<>();
        List<Genre> genreList = new ArrayList<>();

        for (String inspiredByName1 : inspiredByName) {
            InspiredBy inspiredBy = (InspiredBy) inspiredByRepository.findByName(inspiredByName1);
            if (inspiredBy != null) {
                inspiredByList.add(inspiredBy);
            }
        }


        for (String talentName : TalentName) {
            Talent talent = (Talent) TalentRepo.findByName(talentName);
            if (talent != null) {
                talentList.add(talent);
            }
        }


        for (String genreName : GenreName) {
            Genre genre = (Genre) GenreRepo.findByName(genreName);
            if (genre != null) {
                genreList.add(genre);
            }
        }


        savedUser.setInspiredBy((InspiredBy) inspiredByList);
        savedUser.setTalent((Talent) talentList);
        savedUser.setGenre((Set<Genre>) genreList);

        savedUser.setRole(roleRepo.findByName("CUSTOMER"));


        Repo.save(savedUser);
        return UserMapper.maptoUserDto(savedUser);
   }
}
