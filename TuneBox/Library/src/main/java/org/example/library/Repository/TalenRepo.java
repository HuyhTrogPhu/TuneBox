package org.example.library.Repository;

import org.example.library.model.Talent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TalenRepo extends JpaRepository<Talent,Long> {

}
