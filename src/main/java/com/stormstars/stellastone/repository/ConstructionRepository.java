package com.stormstars.stellastone.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stormstars.stellastone.model.atelier.Fusee;

public interface ConstructionRepository extends JpaRepository<Fusee, Long>{

    Fusee findFuseeById(Long id);

    Fusee findByNomFusee(String nomFusee);

    public Fusee findFuseeByUserId(Long id);

}
