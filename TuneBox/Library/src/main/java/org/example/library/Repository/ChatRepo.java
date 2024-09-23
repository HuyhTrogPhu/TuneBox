package org.example.library.Repository;


import org.example.library.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepo extends JpaRepository<Chat,Long> {

}
