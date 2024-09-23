package org.example.library.Repository;


import org.example.library.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OderDetailRepo extends JpaRepository<OrderDetail,Long> {

}
