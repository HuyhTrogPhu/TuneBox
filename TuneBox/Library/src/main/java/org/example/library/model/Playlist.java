package org.example.library.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String imagePlaylist;

    @Size(min = 10, max = 1000)
    private String description;

    private String type;

    private LocalDate createDate;

    private boolean report;

    private LocalDate reportDate;

    private boolean status;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "playlist_track",joinColumns = @JoinColumn(name = "playlist_id"),
    inverseJoinColumns = @JoinColumn(name = "track_id"))

    private Set<Track> tracks = new HashSet<>(); //tao 1 tap hop rong

    @ManyToOne
    @JoinColumn(name = "playlist_creator_id", nullable = false)
    private User creator;
}
