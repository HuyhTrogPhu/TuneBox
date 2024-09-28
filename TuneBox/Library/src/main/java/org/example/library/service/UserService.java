package org.example.library.service;



import org.example.library.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto Register(UserDto userdto, String[] inspiredByName,String[] TalentName,String[] GenreName);
}
