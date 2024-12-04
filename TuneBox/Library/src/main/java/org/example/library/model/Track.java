package org.example.library.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    @Column(columnDefinition = "LONGTEXT")
    private String trackImage;

    @Column(columnDefinition = "LONGTEXT")
    private String trackFile;

    private String description;



    private boolean status = true;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createDate;

    private boolean report; // delete after

    private Date reportDate; // delete after

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "genre_id", referencedColumnName = "genre_id")
    @JsonBackReference
    private Genre genre;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User creator;


    @ManyToMany(mappedBy = "tracks",fetch = FetchType.LAZY)
    private Set<Albums> albums;

    @ManyToMany(mappedBy = "tracks")
    private Set<Playlist> playlists;

    @OneToMany(mappedBy = "track", cascade = CascadeType.ALL)
    private Set<Comment> comments;

    @OneToMany(mappedBy = "track", cascade = CascadeType.ALL)
    private Set<Like> likes;

}