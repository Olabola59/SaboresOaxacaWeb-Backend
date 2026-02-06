package com.bluit.tourgatronomico.repository;

import com.bluit.tourgatronomico.model.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
}
