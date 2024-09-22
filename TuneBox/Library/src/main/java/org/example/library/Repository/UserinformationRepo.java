package org.example.library.Repository;

import org.example.library.model.UserInformation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserinformationRepo extends JpaRepository<UserInformation,Long> {

}
