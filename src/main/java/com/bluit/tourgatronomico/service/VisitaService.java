// ======================= SERVICE =======================
package com.bluit.tourgatronomico.service;

import com.bluit.tourgatronomico.model.Visita;
import com.bluit.tourgatronomico.repository.VisitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VisitaService {

    private final VisitaRepository visitaRepository;

    @Autowired
    public VisitaService(VisitaRepository visitaRepository) {
        this.visitaRepository = visitaRepository;
    }

    public List<Visita> findAll() {
        return visitaRepository.findAll();
    }

    public Optional<Visita> findById(Long id) {
        return visitaRepository.findById(id);
    }

    public Visita save(Visita visita) {
        return visitaRepository.save(visita);
    }

    public void delete(Long id) {
        visitaRepository.deleteById(id);
    }
}
