package org.example.library.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.library.model_enum.UserStatus;
import org.example.library.model.*;

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

    private long FriendCount;

    private long AlbumCount;

    private long reportCount;

    private List<Genre> genre;

    private List<InspiredBy> inspiredBy;

    private List<Talent> talent;


    private UserStatus status;

    public UserSocialAdminDto(Long id, String email, String userName, LocalDate createDate, int size, int size1, long postCount, List<TrackDtoSocialAdmin> trackDtos, long odersCount, UserInformation userInformation, long likeCount, long commentCount) {
    }


    //   private List<Report> reports;

}