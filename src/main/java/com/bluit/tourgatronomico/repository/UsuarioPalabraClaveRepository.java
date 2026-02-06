package com.bluit.tourgatronomico.repository;

import com.bluit.tourgatronomico.model.UsuarioPalabraClave;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UsuarioPalabraClaveRepository extends JpaRepository<UsuarioPalabraClave, Long> {
    List<UsuarioPalabraClave> findByUsuarioId(Long usuarioId);
    List<UsuarioPalabraClave> findByPalabraId(Long palabraId);
    boolean existsByUsuarioIdAndPalabraId(Long usuarioId, Long palabraId);
    void deleteByUsuarioIdAndPalabraId(Long usuarioId, Long palabraId);
    //List<UsuarioPalabraClave> findByPalabraId(Long palabraId);

}