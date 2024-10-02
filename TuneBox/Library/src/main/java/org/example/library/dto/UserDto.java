package org.example.library.dto;


import jakarta.persistence.*;
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
    private String password;
    private String resetToken;
    private String token;
    private String newPassword;
    private boolean report;
    private Date createDate;
    private String reason;
    private Collection<Role> role;
    private String status;
    private Set<Genre> genre;
    private InspiredBy inspiredBy;
    private Talent talent;
    private UserInformation userInformation;
    private Set<Block> blocker;
    private Set<Block> blocked;
    private Set<Follow> followers;
    private Set<Follow> following;
    private List<Order> orderList;
    private Set<Long> trackIds;
    private Set<Long> albumIds;
    private Set<Chat> sentChats;
    private Set<Chat> receivedChats;
    private Set<Message> messages;

    public UserDto(Long id, String userName) {
        this.id = id;
        this.userName = userName;
    }
}