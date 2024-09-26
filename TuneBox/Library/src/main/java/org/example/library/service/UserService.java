package org.example.library.service;



import org.example.library.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto Register(UserDto userdto, Long inspiredById,Long TalentId,Long GenreId);
}
