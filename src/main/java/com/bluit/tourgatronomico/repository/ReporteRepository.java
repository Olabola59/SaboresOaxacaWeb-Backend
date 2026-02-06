package com.bluit.tourgatronomico.repository;

import com.bluit.tourgatronomico.model.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Long> {
    
}
    
