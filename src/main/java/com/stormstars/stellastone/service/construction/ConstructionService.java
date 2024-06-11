package com.stormstars.stellastone.service.construction;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stormstars.stellastone.model.atelier.Fusee;
import com.stormstars.stellastone.repository.ConstructionRepository;

@Service
public class ConstructionService implements IConstructionService {

    @Autowired
    ConstructionRepository cRepo;

    @Override
    public Fusee findById(Long id) {
        return cRepo.findFuseeById(id);
    }

    @Override
    public Fusee findByNomFusee(String nomFusee) {
        return cRepo.findByNomFusee(nomFusee);
    }

    @Override
    public List<Fusee> findAll() {
        return (List<Fusee>) cRepo.findAll();
    }

    @Override
    public void update(Fusee u) {
        cRepo.save(u);
    }

    @Override
    public Fusee findFuseebyUserId(Long id) {
        return cRepo.findFuseeByUserId(id);
    }
}
