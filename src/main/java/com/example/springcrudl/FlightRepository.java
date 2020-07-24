package com.example.springcrudl;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
@Repository
public interface FlightRepository extends CrudRepository<Flight, Long> {
    List<Flight> findByDate(Date date);
    List<Flight> findByPilotId(Pilot pilot);
}
