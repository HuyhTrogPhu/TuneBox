package org.example.library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDto {
   private Long Userid;
   private String email;
   private String userName;
   private String avatar;
   private LocalDateTime joinDate;
}
