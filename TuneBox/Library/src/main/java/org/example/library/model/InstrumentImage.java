package org.example.library.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class InstrumentImage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String imagePath;

    @ManyToOne
    @JoinColumn(name = "instrument_id")
    private Instrument instrument;

}
