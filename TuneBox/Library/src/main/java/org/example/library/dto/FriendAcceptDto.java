package org.example.library.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FriendAcceptDto {
    private Long userId;

    private Long friendId;

    private String avatar;

    private String name;

    private String userName;


}