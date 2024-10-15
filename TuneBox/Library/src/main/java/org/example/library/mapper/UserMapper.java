package org.example.library.mapper;

import org.example.library.dto.UserDto;
import org.example.library.model.*;

import java.util.Set;
import java.util.stream.Collectors;

public class UserMapper {

    // Map User to UserDto
    public static UserDto mapToUserDto(User user) {
        // Extract genre IDs from the Genre Set
        Set<Long> genreIds = user.getGenre() != null ?
                user.getGenre().stream().map(Genre::getId).collect(Collectors.toSet()) :
                null;

        // Extract talent IDs (if Talent is a collection)
        Set<Long> talentIds = user.getTalent() != null ?
                user.getTalent().stream().map(Talent::getId).collect(Collectors.toSet()) :
                null;

        // Extract InspiredBy IDs (if InspiredBy is a collection)
        Set<Long> inspiredByIds = user.getInspiredBy() != null ?
                user.getInspiredBy().stream().map(InspiredBy::getId).collect(Collectors.toSet()) :
                null;

        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getUserName(),
                user.getUserNickname(),
                user.getPassword(),
                user.isReport(),
                user.getCreateDate(),
                user.getReason(),
                user.getUserInformation(),
                inspiredByIds,   // Multiple InspiredBy IDs
                talentIds,       // Multiple Talent IDs
                genreIds,        // Multiple Genre IDs
                user.getRole(),
//                user.getBlocker(),
//                user.getBlocked(),
//                user.getFollowing(),
//                user.getFollowers(),
//                user.getOrderList(),
//                user.getTracks(),
//                user.getAlbums(),
                user.getSentChats(),
                user.getReceivedChats(),
                user.getMessages(),
                user.getResetToken(),
                user.getToken(),
                user.getNewPassword()
        );
    }


    // Map UserDto to User
    public static User mapToUser(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setEmail(userDto.getEmail());
        user.setUserName(userDto.getUserName());
        user.setUserNickname(userDto.getUserNickname());
        user.setPassword(userDto.getPassword());
        user.setReport(userDto.isReport());
        user.setCreateDate(userDto.getCreateDate());
//        user.setReason(userDto.getReason());

        // Map Talent from talentIds (map back to Talent objects)
//        if (userDto.getTalentIds() != null && !userDto.getTalentIds().isEmpty()) {
//            Set<Talent> talents = userDto.getTalentIds().stream().map(id -> {
//                Talent talent = new Talent();
//                talent.setId(id);
//                return talent;
//            }).collect(Collectors.toSet());
//            user.setTalent(talents);
//        }
//
//        // Map InspiredBy from inspiredByIds (map back to InspiredBy objects)
//        if (userDto.getInspiredByIds() != null && !userDto.getInspiredByIds().isEmpty()) {
//            Set<InspiredBy> inspiredBys = userDto.getInspiredByIds().stream().map(id -> {
//                InspiredBy inspiredBy = new InspiredBy();
//                inspiredBy.setId(id);
//                return inspiredBy;
//            }).collect(Collectors.toSet());
//            user.setInspiredBy(inspiredBys);
//        }
//
//        // Map Genre from genreIds (map back to Genre objects)
//        if (userDto.getGenreIds() != null && !userDto.getGenreIds().isEmpty()) {
//            Set<Genre> genres = userDto.getGenreIds().stream().map(id -> {
//                Genre genre = new Genre();
//                genre.setId(id);
//                return genre;
//            }).collect(Collectors.toSet());
//            user.setGenre(genres);
//        }

        // Set other fields
//        user.setRole(userDto.getRole());
//        user.setBlocker(userDto.getBlocker());
//        user.setBlocked(userDto.getBlocked());
//        user.setFollowing(userDto.getFollowing());
//        user.setFollowers(userDto.getFollowers());
//        user.setOrderList(userDto.getOrderList());
//        user.setTracks(userDto.getTracks());
//        user.setAlbums(userDto.getAlbums());
//        user.setSentChats(userDto.getSentChats());
//        user.setReceivedChats(userDto.getReceivedChats());
//        user.setMessages(userDto.getMessages());
        user.setResetToken(userDto.getResetToken());
        user.setToken(userDto.getToken());
        user.setNewPassword(userDto.getNewPassword());

        return user;
    }
    public UserDto toDto(User user) {
        return new UserDto(user.getId());
    }

}
