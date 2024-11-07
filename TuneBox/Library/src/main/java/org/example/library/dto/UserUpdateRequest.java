package org.example.library.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.library.model.UserInformation;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest {
    private String userName;
    private UserInfoUpdateDto userInformation;
    private List<Long> inspiredBy; // New field for inspiredBy IDs
    private List<Long> talent; // New field for talent IDs
    private List<Long> genre; // New field for genre IDs
}
