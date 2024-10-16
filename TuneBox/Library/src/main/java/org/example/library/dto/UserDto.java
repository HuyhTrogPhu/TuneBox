package org.example.library.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.library.model.*;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @JsonIgnore
    private Set<Chat> sentChats;
    @JsonIgnore
    private Set<Chat> receivedChats;
    @JsonIgnore
    private List<Message> messages;

    private String content;
    private UserDto senderId;
    private UserDto receiverId;
    private LocalDateTime creationDate;


    public UserDto(Long id) {
        this.id = id;
    }


    public String getUserNameOrEmail() {
        if ((userName == null || userName.isEmpty()) && (email == null || email.isEmpty())) {
            throw new IllegalArgumentException("Username or email must not be null or empty");
        }
        return (userName != null && !userName.isEmpty()) ? userName : email;
    }


    public UserDto(Long id, String userName) {
        this.id = id;
        this.userName = userName;
    }


    // contractor for register
    public UserDto(String userName, String email, String encodedPassword, List<Long> talents, List<Long> genres, List<Long> inspiredBys) {
        this.userName = userName;
        this.email = email;
        this.password = encodedPassword;
        this.talent = new HashSet<>(talents);
        this.genre = new HashSet<>(genres);
        this.inspiredBy = new HashSet<>(inspiredBys);
    }

    // contractor for mapper
    public UserDto(Long id, String email, String userName, String password, boolean report, LocalDate createDate, Long aLong, Set<Long> inspiredByIds, Set<Long> talentIds, Set<Long> genreIds, Object o, Set<Block> blocker, Set<Block> blocked, Set<Follow> following, Set<Follow> followers, List<Order> orderList, Set<Track> tracks, Set<Albums> albums, Set<Chat> sentChats, Set<Chat> receivedChats, List<Message> messages) {
        this.id = id;
        this.email = email;
        this.userName = userName;
        this.password =  password;
        this.report = report;
        this.createDate = createDate;
        this.userInformationId = aLong;
        this.inspiredBy = new HashSet<>(inspiredByIds);
        this.talent = new HashSet<>(talentIds);
        this.genre = new HashSet<>(genreIds);
        this.role = (Collection<Role>) o;
        this.blocker = blocker;
        this.blocked = blocked;
        this.following = following;
        this.followers = followers;
        this.orderList = orderList;
        this.tracks = tracks;
        this.albums = albums;
        this.sentChats = sentChats;
        this.receivedChats = receivedChats;
        this.messages = messages;
    }
}