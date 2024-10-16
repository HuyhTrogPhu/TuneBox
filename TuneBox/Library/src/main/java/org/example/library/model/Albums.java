package org.example.library.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
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
public class Albums {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "albums_id")
    private Long id;

    private String title;

    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private String albumImage;

    @Size(min = 10, max = 1000)
    private String description;

    private Date releaseDate;

    @Column(name = "created_at", nullable = false)
    private LocalDate createDate;

    private boolean report;

    private Date reportDate;

    private String status;

    @ManyToOne
    @JoinColumn(name = "genre_id", nullable = false)
    private Genre genre;

    @ManyToOne
    @JoinColumn(name = "albumCreator_id", nullable = false)
    @JsonIgnore
    private User creator;

    @OneToMany(mappedBy = "albums", cascade = CascadeType.ALL)
    private Set<Track> tracks;

    @ManyToOne
    @JoinColumn(name = "album_style_id")
    private AlbumStyle albumStyle;


}
