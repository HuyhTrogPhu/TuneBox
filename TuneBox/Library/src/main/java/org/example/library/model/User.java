package org.example.library.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@Entity
@JsonInclude(JsonInclude.Include.NON_EMPTY)

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long id;

    private String email;

    private String userName;

    private String password;

    private boolean report;

    private LocalDate createDate;


    @OneToOne(cascade = CascadeType.ALL)
    @JsonManagedReference
    @JoinColumn(name = "user_information_id", referencedColumnName = "id")
    private UserInformation userInformation;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "user_inspired_by",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "inspired_id", referencedColumnName = "inspired_id"))
    private Set<InspiredBy> inspiredBy;


    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "user_talent",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "talent_id", referencedColumnName = "talent_id"))
    private Set<Talent> talent;


    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "user_genre", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private Set<Genre> genre;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", referencedColumnName = "role_id")
    @JsonBackReference
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Report> reports;


    @OneToMany(mappedBy = "blocker")
    private Set<Block> blocker;

    @OneToMany(mappedBy = "blocked")
    private Set<Block> blocked;

    @OneToMany(mappedBy = "follower")
    private Set<Follow> following;

    @OneToMany(mappedBy = "followed")
    private Set<Follow> followers;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Order> orderList;

    @OneToMany(mappedBy = "creator")
    private Set<Track> tracks;

    @OneToMany(mappedBy = "creator")
    private Set<Albums> albums;

    @OneToMany(mappedBy = "sender")
    private Set<Chat> sentChats;

    @OneToMany(mappedBy = "receiver")
    private Set<Chat> receivedChats;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Message> messages;

    @OneToMany(mappedBy = "user")
    private Set<Like> likes;

    @OneToMany(mappedBy = "user")
    private Set<Comment> comments;

    public User(Long blockerId) {
        this.id = blockerId;
    }

}