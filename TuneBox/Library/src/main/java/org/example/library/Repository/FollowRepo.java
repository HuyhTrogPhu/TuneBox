package org.example.library.Repository;


import org.example.library.model.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepo extends JpaRepository<Follow,Long> {

}
