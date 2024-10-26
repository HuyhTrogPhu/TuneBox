package org.example.library.dto;


import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.library.model.User;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BlockDto {
    private Long id;
    private Long blockerId; // Chỉ cần ID
    private Long blockedId; // Chỉ cần ID
    private LocalDateTime createBlock;
}
