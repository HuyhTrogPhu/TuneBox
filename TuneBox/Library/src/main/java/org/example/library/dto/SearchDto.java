package org.example.library.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchDto {

//    user
    private Long id;
    private String name;

//    usserInfo
    private String avatar;
    private String userName;

//    track
    private Long trackId;
    private String trackName;
    private String trackDescription;
    private String imageTrack;
    private String userNameTrack;

//    album
    private Long albumId;
    private String albumTitle;
    private String albumDescription;
    private String imageAlbum;
    private String userNameAlbum;

//    playlist
    private Long playListId;
    private String playListTitle;
    private String imagePlaylist;
    private String userNamePlaylist;

    public SearchDto(Long id, String avatar, String name) {
        this.id = id;
        this.avatar = avatar;
        this.name = name;
    }

    // Constructor cho user
    public SearchDto(Long id, String name, String avatar, String userName) {
        this(id, name, avatar, userName,null, null, null, null, null, null, null, null, null, null, null, null, null,null);
    }

    // Constructor cho track
    public SearchDto(Long id, Long trackId, String trackName, String trackDescription, String imageTrack, String userNameTrack) {
        this(null, null, null,  null,trackId, trackName, trackDescription, imageTrack, userNameTrack, null, null, null, null, null, null, null, null, null);
    }

    // Constructor cho album
    public SearchDto(Long albumId, String albumTitle, String albumDescription, String imageAlbum, String userNameAlbum) {
        this(null, null, null, null, null, null, null, null,  null,albumId, albumTitle, albumDescription, imageAlbum, userNameAlbum, null, null, null, null);
    }

    // Constructor cho playlist
    public SearchDto(String name,Long playListId, String playListTitle, String imagePlaylist, String userNamePlaylist) {
        this(null, name, null,  null, null, null, null, null, null, null, null, null, null, null, playListId, playListTitle, imagePlaylist, userNamePlaylist);
    }
}
