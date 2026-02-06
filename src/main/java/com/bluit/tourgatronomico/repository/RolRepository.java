// RolRepository
package com.bluit.tourgatronomico.repository;

import com.bluit.tourgatronomico.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> { // ✅ Long
    Optional<Rol> findByNombre(String nombre);
    List<Rol> findByNombreContainingIgnoreCase(String nombre);
}