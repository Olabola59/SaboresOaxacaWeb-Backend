package com.bluit.tourgatronomico.repository;

import com.bluit.tourgatronomico.model.TipoLugar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoLugarRepository extends JpaRepository<TipoLugar, Long> {
}