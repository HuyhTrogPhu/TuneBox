package org.example.library.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Track {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "track_id")
    private Long id;

    private String name;

    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private String trackImage;

    private String description;

    private boolean status;

    @Column(name = "created_at", nullable = false)
    private LocalDate createDate;

    private boolean report;

    private Date reportDate;

    @ManyToOne
    @JoinColumn(name = "genre_id", nullable = false)
    private Genre genre;



    @ManyToOne(fetch = FetchType.LAZY) // Thiết lập mối quan hệ với User
    @JoinColumn(name = "user_id") // Tên cột trong bảng Post
    private User user;


    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "albums_id")
    private Albums albums;

    @ManyToMany(mappedBy = "tracks")
    private Set<Playlist> playlists;


    @OneToMany(mappedBy = "track", cascade = CascadeType.ALL)
    private Set<Like> likes;

}
