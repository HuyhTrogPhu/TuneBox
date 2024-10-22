package org.example.library.dto;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.library.model.Chat;
import org.example.library.model.Street;
import org.example.library.model.User;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NumberDto {

    private Long id;

    private Street street;

    private String name;
    
}
