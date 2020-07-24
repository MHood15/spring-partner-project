package com.example.springcrudl;

import org.springframework.data.repository.CrudRepository;

public interface PlaneRepository extends CrudRepository<Plane, Long> {
}
