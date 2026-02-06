package com.bluit.tourgatronomico.repository;

import com.bluit.tourgatronomico.model.PlatilloPalabraClave;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlatilloPalabraClaveRepository extends JpaRepository<PlatilloPalabraClave, Long> {
  List<PlatilloPalabraClave> findByPlatilloId(Long platilloId);
  boolean existsByPlatilloIdAndPalabraId(Long platilloId, Long palabraId);
  void deleteByPlatilloIdAndPalabraId(Long platilloId, Long palabraId);
}
