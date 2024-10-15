package org.example.library.repository;

import org.example.library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long userId);

    // get user by username or email
//    @Query("select u from User u where u.userName = ?1 or u.email = ?2")
    Optional<User> findByUserNameOrEmail(String userName, String email);

    User findByEmail(String email);

    List<User> findAll();

}
