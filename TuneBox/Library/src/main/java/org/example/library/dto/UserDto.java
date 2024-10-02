package org.example.library.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.library.model.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String email;
    private String userName;
    private String password;
    private boolean report;
    private Date createDate;
    private String reason;
    private Collection<Role> role;
    private Set<Block> blocker;
    private Set<Block> blocked;
    private Set<Follow> following;
    private List<Order> orderList;
    private Set<Track> tracks;
    private Set<Albums> albums;
    private Set<Chat> sentChats;
    private Set<Chat> receivedChats;
    private Set<Message> messages;
    }