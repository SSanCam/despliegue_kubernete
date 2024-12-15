package com.es.ApiLol.repository;

import com.es.ApiLol.model.Campeon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CampeonRepository extends JpaRepository<Campeon, Long> {
    Optional<Campeon> findByNombre(String nombre);
}
