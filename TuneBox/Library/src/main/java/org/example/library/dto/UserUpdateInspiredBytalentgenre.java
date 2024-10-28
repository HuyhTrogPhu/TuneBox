package org.example.library.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateInspiredBytalentgenre {
    private Set<Long> inspiredBy;
    private Set<Long> talent;
    private Set<Long> genre;
}
