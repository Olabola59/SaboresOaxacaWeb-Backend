package com.bluit.tourgatronomico.service;

import com.bluit.tourgatronomico.model.Favorito;
import com.bluit.tourgatronomico.repository.FavoritoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class FavoritoService {
    
    private final FavoritoRepository favoritoRepository;

    @Autowired
    public FavoritoService(FavoritoRepository favoritoRepository) {
        this.favoritoRepository = favoritoRepository;
    }

    public List<Favorito> findAll() {
        return favoritoRepository.findAll();
    }

    public Optional<Favorito> findById(Long id) {
        return favoritoRepository.findById(id);
    }

    public List<Favorito> findByUsuarioId(Long usuarioId) {
        return favoritoRepository.findByUsuarioId(usuarioId);
    }

    public Optional<Favorito> findByUsuarioIdAndPlatilloId(Long usuarioId, Long platilloId) {
        return favoritoRepository.findByUsuarioIdAndPlatilloId(usuarioId, platilloId);
    }

    public boolean existsByUsuarioIdAndPlatilloId(Long usuarioId, Long platilloId) {
        return favoritoRepository.existsByUsuarioIdAndPlatilloId(usuarioId, platilloId);
    }

    public Favorito save(Favorito favorito) {
        return favoritoRepository.save(favorito);
    }

    @Transactional
    public void deleteByUsuarioIdAndPlatilloId(Long usuarioId, Long platilloId) {
        favoritoRepository.deleteByUsuarioIdAndPlatilloId(usuarioId, platilloId);
    }

    public void delete(Long id) {
        favoritoRepository.deleteById(id);
    }
}