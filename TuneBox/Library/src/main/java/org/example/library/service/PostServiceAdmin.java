package org.example.library.service;

import org.example.library.dto.PostAdminDto;

import java.util.List;

public interface PostServiceAdmin {
    List<PostAdminDto> getTopPostsByInteractions();

    List<PostAdminDto> findPostsWithImages();

    List<PostAdminDto> findPostsWithoutImages();

    List<PostAdminDto> findAllByOrderByCreatedAtDesc();

}
