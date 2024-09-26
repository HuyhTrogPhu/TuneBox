package org.example.library.service.implement;


import lombok.AllArgsConstructor;
import org.example.library.dto.UserDto;
import org.example.library.mapper.UserMapper;
import org.example.library.model.User;
import org.example.library.repository.UserRepository;
import org.example.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    public UserRepository Repo;

    @Override
    public UserDto Register(UserDto userdto) {
        User user = UserMapper.maptoUser(userdto);
        User savedUser = Repo.save(user);
        return UserMapper.maptoUserDto(savedUser);
    }
}
