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
public class Instrument {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "instrument_id")
    private Long id;

    private String name;

    private double costPrice;

    private int quantity;

    private String color;

    private boolean status;

    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private String image;

    @Size(min = 10, max = 1000)
    private String description;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "category_name", referencedColumnName = "category_id")
    private CategoryIns categoryIns;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "brand_name", referencedColumnName = "brand_id")
    private Brand brand;



}
