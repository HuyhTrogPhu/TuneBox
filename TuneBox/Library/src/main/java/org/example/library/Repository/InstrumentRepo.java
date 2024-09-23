package org.example.library.Repository;


import org.example.library.model.Instrument;
import org.springframework.data.jpa.repository.JpaRepository;


public interface InstrumentRepo extends JpaRepository<Instrument,Long> {

}
