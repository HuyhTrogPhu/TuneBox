package org.example.library.Repository;

import org.example.library.model.InspiredBy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InspiredByRepo extends JpaRepository<InspiredBy,Long> {

}
