package org.example.library.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Block {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "block_id")
    private Long id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "blocker_id", nullable = false)
    private User blocker;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "blocked_id", nullable = false)
    private User blocked;

    @Column(name = "create_block", nullable = false)
    private LocalDateTime createBlock = LocalDateTime.now();
}
