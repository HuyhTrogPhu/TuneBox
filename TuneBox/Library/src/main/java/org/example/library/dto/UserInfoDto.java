package org.example.library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDto {
   private Long Userid;
   private String email;
   private String userName;
   private String avatar;
   private LocalDateTime joinDate;
   //new entities
   private long followedUsers;
   private long followingUsers;
   private long orderCount;
   private long albums;
   private long tracks;
   private List<String> inspiredBy;
   private List<String> talent;
   private List<String> genre;
}
