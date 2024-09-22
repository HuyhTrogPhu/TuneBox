package org.example.library.Repository;

import org.example.library.model.Block;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlockRepo extends JpaRepository<Block,Long> {

}
