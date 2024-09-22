package org.example.library.Repository;


import org.example.library.model.District;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DistrictRepo extends JpaRepository<District,Long> {

}
