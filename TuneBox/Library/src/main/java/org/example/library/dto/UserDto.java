package org.example.library.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.library.model.*;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long id;
    private String email;
    private String userName;
    private String userNickname;
    private String password;
    private boolean report;
    private Date createDate;
    private String reason;

    // Multiple IDs for relationships
    private UserInformation userInformationIds;
    private Set<Long> inspiredByIds;
    private Set<Long> talentIds;
    private Set<Long> genreIds;

    @JsonIgnore
    private Collection<Role> role;

    private Set<Block> blocker;
    private Set<Block> blocked;

    // Following and followers as ID sets to avoid infinite loops
    private Set<Long> followingIds;  // IDs of users this user follows
    private Set<Long> followerIds;   // IDs of users who follow this user

    private List<Order> orderList;
    private Set<Track> tracks;
    private Set<Albums> albums;
    private Set<Chat> sentChats;
    private Set<Chat> receivedChats;
    private Set<Message> messages;

    private String resetToken;
    private String token;
    private String newPassword;

    // Additional constructor for simple mapping
    public UserDto(Long id, String userName) {
        this.id = id;
        this.userName = userName;
    }

    public UserDto(Long id, String email, String userName, String userNickname, boolean report, Date createDate, String reason,
                   UserInformation userInformation, Set<Long> inspiredByIds, Set<Long> talentIds, Set<Long> genreIds,
                   Collection<Role> role, Set<Block> blocker, Set<Block> blocked, Set<Long> followingIds, Set<Long> followerIds,
                   List<Order> orderList, Set<Track> tracks, Set<Albums> albums, Set<Chat> sentChats, Set<Chat> receivedChats,
                   Set<Message> messages, String resetToken, String token, String newPassword) {
        this.id = id;
        this.email = email;
        this.userName = userName;
        this.userNickname = userNickname;
        this.report = report;
        this.createDate = createDate;
        this.reason = reason;
        this.userInformationIds = userInformation;
        this.inspiredByIds = inspiredByIds;
        this.talentIds = talentIds;
        this.genreIds = genreIds;
        this.role = role;
        this.blocker = blocker;
        this.blocked = blocked;
        this.followingIds = followingIds;
        this.followerIds = followerIds;
        this.orderList = orderList;
        this.tracks = tracks;
        this.albums = albums;
        this.sentChats = sentChats;
        this.receivedChats = receivedChats;
        this.messages = messages;
        this.resetToken = resetToken;
        this.token = token;
        this.newPassword = newPassword;
    }
}
