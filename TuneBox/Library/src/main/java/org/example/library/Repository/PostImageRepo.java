package org.example.library.Repository;

import org.example.library.model.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostImageRepo extends JpaRepository<PostImage,Long> {

}
