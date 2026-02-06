package com.bluit.tourgatronomico.repository;

import com.bluit.tourgatronomico.model.LugarPalabraClave;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LugarPalabraClaveRepository extends JpaRepository<LugarPalabraClave, Long> {
    List<LugarPalabraClave> findByLugarId(Long lugarId);
    boolean existsByLugarIdAndPalabraId(Long lugarId, Long palabraId);
    void deleteByLugarIdAndPalabraId(Long lugarId, Long palabraId);
}
