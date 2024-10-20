package org.example.library.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonManagedReference
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_information_id", referencedColumnName = "id")
    private UserInformation userInformation;

    @JsonManagedReference
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "user_inspired_by",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "inspired_id", referencedColumnName = "inspired_id"))
    private Set<InspiredBy> inspiredBy;

    @JsonManagedReference
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "user_talent",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "talent_id", referencedColumnName = "talent_id"))
    private Set<Talent> talent;

    @JsonManagedReference
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "user_genre", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private Set<Genre> genre;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", referencedColumnName = "role_id")
    private Role role;

    @JsonManagedReference
    @OneToMany(mappedBy = "blocker")
    private Set<Block> blocker;

    @JsonManagedReference
    @OneToMany(mappedBy = "blocked")
    private Set<Block> blocked;

    @JsonManagedReference
    @OneToMany(mappedBy = "follower")
    private Set<Follow> following;

    @JsonManagedReference
    @OneToMany(mappedBy = "followed")
    private Set<Follow> followers;

    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Order> orderList;

    @JsonManagedReference
    @OneToMany(mappedBy = "creator")
    private Set<Track> tracks;

    @JsonManagedReference
    @OneToMany(mappedBy = "creator")
    private Set<Albums> albums;

    @JsonManagedReference
    @OneToMany(mappedBy = "sender")
    private Set<Chat> sentChats;

    @JsonManagedReference
    @OneToMany(mappedBy = "receiver")
    private Set<Chat> receivedChats;

    @JsonManagedReference
    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Message> messages;

    @JsonManagedReference
    @OneToMany(mappedBy = "user")
    private Set<Like> likes;

    @JsonManagedReference
    @OneToMany(mappedBy = "user")
    private Set<Comment> comments;

    public User(Long blockerId) {
        this.id = blockerId;
    }

}