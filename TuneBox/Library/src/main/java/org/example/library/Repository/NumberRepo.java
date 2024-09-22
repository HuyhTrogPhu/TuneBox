package org.example.library.Repository;

import org.example.library.model.Number;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NumberRepo extends JpaRepository<Number,Long> {

}
