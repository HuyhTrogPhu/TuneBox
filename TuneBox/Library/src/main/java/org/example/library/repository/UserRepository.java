package org.example.library.repository;

import org.example.library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long userId);

    Optional<User> findByEmail(String email);

    Optional<User> findByResetToken(String resetToken);

    Optional<User> findByUserName(String userName);

    List<User> findAll();
    long countByIdNotNull();

//    @Query("SELECT u FROM User u LEFT JOIN u.post p GROUP BY u ORDER BY COUNT(p) DESC")
//    List<User> UserOderByPostHightoLow();



}
