package org.example.library.dto;


import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.library.model.Country;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CountryDto {

    private Long id;

    private String name;
}
