package org.example.library.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.library.model.Report;
import org.example.library.model.UserInformation;
import org.example.library.model_enum.UserStatus;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSocialAdminDto {
    private Long id;

    private String email;

    private String userName;

    private LocalDate createDate;

    private long followingCount;

    private long followerCount;

    private long postCount;

    private List<TrackDtoSocialAdmin> Tracks;

    private long OdersCount;

    private UserInformation userInformation;

    private long commentCount;

    private long LikeCount;

    private UserStatus status;

    //   private List<Report> reports;

}
