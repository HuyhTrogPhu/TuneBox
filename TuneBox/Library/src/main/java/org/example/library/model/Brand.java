package org.example.library.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "instrument_brand")
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "brand_id")
    private Long id;

    private String name;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private String brandImage;

    @Size(min = 10, max = 1000)
    private String description;

    private boolean status;
}
