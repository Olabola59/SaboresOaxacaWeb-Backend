package com.bluit.tourgatronomico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bluit.tourgatronomico.model.Recomendacion;

public interface RecomendacionRepository extends JpaRepository<Recomendacion, Long> {
}
