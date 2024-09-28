package org.example.library.mapper;


import org.example.library.dto.UserDto;
import org.example.library.model.User;

public class UserMapper {

    public static UserDto maptoUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getUserName(),
                user.getPassword(),
                user.isReport(),
                user.getCreateDate(),
                user.getReason(),
                user.getUserInformation(),
                user.getInspiredBy(),
                user.getTalent(),
                user.getGenre(),
                user.getRole(),
                user.getBlocker(),
                user.getBlocked(),
                user.getFollowing(),
                user.getFollowers(),
                user.getOrderList(),
                user.getTracks(),
                user.getAlbums(),
                user.getSentChats(),
                user.getReceivedChats(),
                user.getMessages());

    }

    public static User maptoUser(UserDto user) {
        return new User(
                user.getId(),
                user.getEmail(),
                user.getUserName(),
                user.getPassword(),
                user.isReport(),
                user.getCreateDate(),
                user.getReason(),
                user.getUserInformation(),
                user.getInspiredBy(),
                user.getTalent(),
                user.getGenre(),
                user.getRole(),
                user.getBlocker(),
                user.getBlocked(),
                user.getFollowing(),
                user.getFollowers(),
                user.getOrderList(),
                user.getTracks(),
                user.getAlbums(),
                user.getSentChats(),
                user.getReceivedChats(),
                user.getMessages()
        );
    }

}
