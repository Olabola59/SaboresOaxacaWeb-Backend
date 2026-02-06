package com.bluit.tourgatronomico.service;

import com.bluit.tourgatronomico.model.Evento;
import com.bluit.tourgatronomico.repository.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventoService {

    @Autowired
    private EventoRepository eventoRepository;

    public List<Evento> obtenerTodos() {
        return eventoRepository.findAll();
    }

    public Optional<Evento> obtenerPorId(Integer id) {
        return eventoRepository.findById(id);
    }

    public Evento guardar(Evento evento) {
        return eventoRepository.save(evento);
    }

    public void eliminar(Integer id) {
        eventoRepository.deleteById(id);
    }
}
