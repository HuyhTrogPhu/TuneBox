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
import org.example.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public UserDto Register(RequestSignUpModel requestSignUpModel) {
        User savedUser = new User();

        savedUser.setEmail(requestSignUpModel.getUserDto().getEmail());
        savedUser.setPassword(crypt.encode(requestSignUpModel.getUserDto().getPassword()));
        savedUser.setUserName(requestSignUpModel.getUserDto().getUserName());



        List<InspiredBy> inspiredByList = new ArrayList<>();
        List<Talent> talentList = new ArrayList<>();
        List<Genre> genreList = new ArrayList<>();

        for (String inspiredByName1 : requestSignUpModel.getListInspiredBy()) {
            List<InspiredBy> inspiredBy = inspiredByRepository.findByName(inspiredByName1);
            if (inspiredBy != null) {
                inspiredByList.addAll(inspiredBy);
            }
        }


        for (String talentName : requestSignUpModel.getListTalent()) {
            List<Talent> talent =  TalentRepo.findByName(talentName);
            if (talent != null) {
                talentList.addAll(talent);
            }
        }


        for (String genreName : requestSignUpModel.getGenreBy()) {
            List<Genre> genre =  GenreRepo.findByName(genreName);
            if (genre != null) {
                genreList.addAll(genre);
            }
        }


        savedUser.setInspiredBy(inspiredByList.get(0));
        savedUser.setTalent(talentList.get(0));
        savedUser.setGenre(Set.copyOf(genreList));

        savedUser.setRole(roleRepo.findByName("CUSTOMER"));


        Repo.save(savedUser);
        return UserMapper.maptoUserDto(savedUser);
   }

    @Override
    public Optional<User> findById(Long userId) {
        return Repo.findById(userId);
    }
}
