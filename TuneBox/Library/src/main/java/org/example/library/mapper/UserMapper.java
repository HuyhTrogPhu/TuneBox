package org.example.library.mapper;


import org.example.library.dto.UserDto;
import org.example.library.model.User;

public class UserMapper {

    public static UserDto maptoUserDto(User user) {
        if (user == null)
            return null;

        UserDto userdto = new UserDto();

        userdto.setId(user.getId());
        userdto.setEmail(user.getEmail());
        userdto.setUserName(user.getUserName());
        userdto.setPassword(user.getPassword());
        userdto.isReport();
        userdto.setCreateDate(user.getCreateDate());
        userdto.setReason(user.getReason());
        userdto.setUserInformation(user.getUserInformation());
        userdto.setInspiredBy(user.getInspiredBy());
        userdto.setTalent(user.getTalent());
        userdto.setResetToken(user.getResetToken());
        userdto.setToken(user.getToken());
        userdto.setNewPassword(user.getNewPassword());
        userdto.setGenre(user.getGenre());
        userdto.setRole(user.getRole());
        userdto.setBlocker(user.getBlocker());
        userdto.setBlocked(user.getBlocked());
        userdto.setFollowing(user.getFollowing());
        userdto.setFollowers(user.getFollowers());
        userdto.setOrderList(user.getOrderList());
        userdto.setTracks(user.getTracks());
        userdto.setAlbums(user.getAlbums());
        userdto.setSentChats(user.getSentChats());
        userdto.setReceivedChats(user.getReceivedChats());
        userdto.setMessages(user.getMessages());
        return userdto;
    }


    public static User maptoUser(UserDto userdto) {
            if (userdto == null)
                return null;
           User user = new User();
           user.setId(userdto.getId());
           user.setEmail(userdto.getEmail());
           user.setUserName(userdto.getUserName());
           user.setPassword(userdto.getPassword());
           user.setResetToken(userdto.getResetToken());
           user.setToken(userdto.getToken());
           user.setNewPassword(userdto.getNewPassword());
           return user;
    }

}