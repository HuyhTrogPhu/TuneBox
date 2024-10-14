package org.example.library.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.library.model.Message;
import org.example.library.model.User;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatDto {

    private Long id;

    private LocalDate creationDate;

    private User sender;

    private User receiver;

    private Set<Message> messages;
}
