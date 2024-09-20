package org.example.library.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "album_style")
@Entity
public class AlbumStyle {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "album_style_id")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "albumStyle")
    private Set<Albums> albums;

}
