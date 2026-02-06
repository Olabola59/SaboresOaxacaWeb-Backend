package com.bluit.tourgatronomico.repository;

import com.bluit.tourgatronomico.model.Dispositivo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface DispositivoRepository extends JpaRepository<Dispositivo, Long> {
    List<Dispositivo> findByUsuarioId(long usuarioId);
    boolean existsByTokenFcm(String tokenFcm);
    Optional<Dispositivo> findByTokenFcm(String tokenFcm);
}
