package org.example.library.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    private Date createDate;

    private String reason;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "inspire_id", referencedColumnName = "inspire_id")
    private InspiredBy inspiredBy;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "talent_id", referencedColumnName = "talent_id")
    private Talent talent;

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

    @OneToMany(mappedBy = "sender")
    private Set<Message> messages;
    

}
