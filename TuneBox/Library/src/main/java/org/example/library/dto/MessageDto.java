package org.example.library.dto;


import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.library.model.Brand;
import org.example.library.model.CategoryIns;
import org.example.library.model.Chat;
import org.example.library.model.User;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {

    private Long id;

    private String message;

    private LocalDateTime dateTime;

    private Chat chat;

    private User user;

}
