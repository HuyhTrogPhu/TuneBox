package org.example.library.dto;

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
    private Set<Long> genreIds;      // Multiple Genre IDs
    private Set<Long> trackIds;
    private Set<Long> albumIds;

    private Collection<Role> role;
    private Set<Block> blocker;
    private Set<Block> blocked;
    private Set<Follow> following;
    private Set<Follow> followers;
    private List<Order> orderList;
    private Set<Chat> sentChats;
    private Set<Chat> receivedChats;
    private Set<Message> messages;
    private String resetToken;
    private String token;
    private String newPassword;

    public UserDto(Long id, String userName) {
        this.id = id;
        this.userName = userName;
    }

}