package org.example.library.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.library.model.*;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.*;

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

    private LocalDate createDate = LocalDate.now();


    private Long userInformationId;

    private Set<Long> inspiredBy;

    private Set<Long> talent;

    private Set<Long> genre;

    @JsonIgnore
    private Collection<Role> role;

    private Set<Block> blocker;

    private Set<Block> blocked;

    private Set<Follow> following;

    private Set<Follow> followers;

    private List<Order> orderList;

    private Set<Track> tracks;

    private Set<Albums> albums;

    private Set<Chat> sentChats;

    private Set<Chat> receivedChats;

    private Set<Message> messages;



    public UserDto(Long id, String userName) {
        this.id = id;
        this.userName = userName;
    }
}