package org.example.library.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @Column(columnDefinition = "LONGBLOB")
    private String trackImage;

    @Column(columnDefinition = "LONGBLOB")
    private String trackFile;

    private String description;

    private boolean status;

    @Column(name = "created_at", nullable = false)
    private LocalDate createDate = LocalDate.now();

    private boolean report;

    private Date reportDate;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "genre_id", referencedColumnName = "genre_id")
    private Genre genre;


    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User creator;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "albums_id", nullable = true)
    @JsonIgnore
    private Albums albums;

    @ManyToMany(mappedBy = "tracks")
    @JsonIgnore
    private Set<Playlist> playlists;

    @OneToMany(mappedBy = "track", cascade = CascadeType.ALL)
    private Set<Comment> comments;

    @OneToMany(mappedBy = "track", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Set<Like> likes;

}