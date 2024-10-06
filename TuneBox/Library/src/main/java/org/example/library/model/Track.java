package org.example.library.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
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

    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private byte[]  musicFile;

    private String description;

    private boolean status;

    @Column(name = "created_at", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate createDate;

    private boolean report;

    private Date reportDate;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "genre_id", referencedColumnName = "genre_id")
    private Genre genre;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "trackCreator_id", nullable = false)
    private User creator;


    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "albums_id", nullable = false)
    private Albums albums;

    @ManyToMany(mappedBy = "tracks")
    private Set<Playlist> playlists;

    @OneToMany(mappedBy = "track", cascade = CascadeType.ALL)
    private Set<Comment> comments;

    @OneToMany(mappedBy = "track", cascade = CascadeType.ALL)
    private Set<Like> likes;

}
