package com.bluit.tourgatronomico.service;

import com.bluit.tourgatronomico.model.TipoLugar;
import com.bluit.tourgatronomico.repository.TipoLugarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoLugarService {

    @Autowired
    private TipoLugarRepository tipoLugarRepository;

    public List<TipoLugar> findAll() {
        return tipoLugarRepository.findAll();
    }

    public TipoLugar save(TipoLugar tipoLugar) {
        return tipoLugarRepository.save(tipoLugar);
    }
}