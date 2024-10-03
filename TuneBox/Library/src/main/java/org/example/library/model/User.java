package org.example.library.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

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
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long id;

    private String email;

    private String userName;
    private String userNickname;
    private String password;

    private boolean report;

    private Date createDate;

    private String reason;

    private String resetToken;

    private String token;

    private String newPassword;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_information_id", referencedColumnName = "id")
    private UserInformation userInformation;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_inspired_by",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "inspired_id", referencedColumnName = "inspired_id"))
    private Set<InspiredBy> inspiredBy;


    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_talent",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "talent_id", referencedColumnName = "talent_id"))
    private Set<Talent> talent;


    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_genre", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private Set<Genre> genre;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "role_id"))
    private Collection<Role> role;

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

    @OneToMany(mappedBy = "user")
    private Set<Message> messages;

    @OneToMany(mappedBy = "user")
    private Set<Like> likes;

    @OneToMany(mappedBy = "user")
    private Set<Comment> comments;


}
