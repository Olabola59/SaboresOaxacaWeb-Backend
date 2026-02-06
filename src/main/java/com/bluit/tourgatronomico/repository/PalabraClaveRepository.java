package com.bluit.tourgatronomico.repository;

import com.bluit.tourgatronomico.model.PalabraClave;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PalabraClaveRepository extends JpaRepository<PalabraClave, Long> {
    List<PalabraClave> findByNombreContainingIgnoreCase(String nombre);
}
