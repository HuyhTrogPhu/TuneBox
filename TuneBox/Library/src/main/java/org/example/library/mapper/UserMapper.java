package org.example.library.mapper;

import org.example.library.dto.UserDto;
import org.example.library.model.*;

import java.util.Set;
import java.util.stream.Collectors;

public class UserMapper {

    public static UserDto mapToUserDto(User user) {
        if (user == null) {
            return null;
        }

        Set<Long> genreIds = user.getGenre() != null ?
                user.getGenre().stream().map(Genre::getId).collect(Collectors.toSet()) :
                null;

        Set<Long> talentIds = user.getTalent() != null ?
                user.getTalent().stream().map(Talent::getId).collect(Collectors.toSet()) :
                null;

        Set<Long> inspiredByIds = user.getInspiredBy() != null ?
                user.getInspiredBy().stream().map(InspiredBy::getId).collect(Collectors.toSet()) :
                null;

        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getUserName(),
                user.getPassword(),
                user.isReport(),
                user.getCreateDate(),
                user.getUserInformation() != null ? user.getUserInformation().getId() : null,
                inspiredByIds,
                talentIds,
                genreIds,
                null,
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

    // Map UserDto to User
    public static User mapToUser(UserDto userDto, UserInformation userInformation, Set<InspiredBy> inspiredBy, Set<Talent> talent, Set<Genre> genre, Role role) {
        if (userDto == null) {
            return null;
        }

        User user = new User();
        user.setId(userDto.getId());
        user.setEmail(userDto.getEmail());
        user.setUserName(userDto.getUserName());
        user.setPassword(userDto.getPassword());
        user.setReport(userDto.isReport());
        user.setCreateDate(userDto.getCreateDate());

        // Map các đối tượng liên quan
        user.setUserInformation(userInformation);
        user.setInspiredBy(inspiredBy);
        user.setTalent(talent);
        user.setGenre(genre);
        user.setRole(role);

        // Set các trường khác
        user.setBlocker(userDto.getBlocker());
        user.setBlocked(userDto.getBlocked());
        user.setFollowing(userDto.getFollowing());
        user.setFollowers(userDto.getFollowers());
        user.setOrderList(userDto.getOrderList());
        user.setTracks(userDto.getTracks());
        user.setAlbums(userDto.getAlbums());
        user.setSentChats(userDto.getSentChats());
        user.setReceivedChats(userDto.getReceivedChats());
        user.setMessages(userDto.getMessages());

        return user;
    }
}
