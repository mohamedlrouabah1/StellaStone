package com.stormstars.stellastone.service.construction;

import java.util.List;

import com.stormstars.stellastone.model.atelier.Fusee;

public interface IConstructionService {
    

    public Fusee findFuseebyUserId(Long id);

    public Fusee findById(Long id);

    public Fusee findByNomFusee(String nomFusee);

    public List<Fusee> findAll();

    public void update(Fusee u) ;



}
