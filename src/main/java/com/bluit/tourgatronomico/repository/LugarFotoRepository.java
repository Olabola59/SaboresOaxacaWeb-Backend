package com.bluit.tourgatronomico.repository;

import com.bluit.tourgatronomico.model.LugarFoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LugarFotoRepository extends JpaRepository<LugarFoto, Long> {
    
}
