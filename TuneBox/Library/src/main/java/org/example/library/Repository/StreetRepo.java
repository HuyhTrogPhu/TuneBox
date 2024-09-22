package org.example.library.Repository;

import org.example.library.model.Street;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StreetRepo extends JpaRepository<Street,Long> {

}
