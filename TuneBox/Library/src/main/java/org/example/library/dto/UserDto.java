package org.example.library.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.library.model.*;

import java.lang.reflect.Array;
import java.util.*;

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

//    private Set<Block> blocker;
//    private Set<Block> blocked;
//    private Set<Follow> following;
//    private Set<Follow> followers;
//    private List<Order> orderList;
//    private Set<Track> tracks;
//    private Set<Albums> albums;
    @JsonIgnore
    private Set<Chat> sentChats;
    @JsonIgnore
    private Set<Chat> receivedChats;
    @JsonIgnore
    private List<Message> messages;
    private String resetToken;
    private String token;
    private String newPassword;

    public UserDto(Long id, String userName) {
        this.id = id;
        this.userName = userName;
    }
    public UserDto(Long id) {
        this.id = id;
    }
}