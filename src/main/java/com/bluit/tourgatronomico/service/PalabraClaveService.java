package com.bluit.tourgatronomico.service;

import com.bluit.tourgatronomico.model.PalabraClave;
import com.bluit.tourgatronomico.repository.PalabraClaveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PalabraClaveService {

    @Autowired
    private PalabraClaveRepository repo;

    public List<PalabraClave> findAll() {
        return repo.findAll();
    }

    public Optional<PalabraClave> findById(Long id) {
        return repo.findById(id);
    }

    public List<PalabraClave> search(String q) {
        return repo.findByNombreContainingIgnoreCase(q);
    }

    public PalabraClave save(PalabraClave p) {
        return repo.save(p);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
