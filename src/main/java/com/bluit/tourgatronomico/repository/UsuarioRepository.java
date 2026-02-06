/*package com.bluit.tourgatronomico.repository;

import com.bluit.tourgatronomico.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);

    List<Usuario> findByNombreContainingIgnoreCase(String nombre);

    Optional<Usuario> findByVerificationToken(String verificationToken);

    List<Usuario> findAllByEmailVerificadoTrue();
}*/

package com.bluit.tourgatronomico.repository;

import com.bluit.tourgatronomico.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);

    List<Usuario> findByNombreContainingIgnoreCase(String nombre);

    Optional<Usuario> findByVerificationToken(String verificationToken);

    // ✅ NUEVO: reset password token
    Optional<Usuario> findByResetPasswordToken(String resetPasswordToken);

    List<Usuario> findAllByEmailVerificadoTrue();
}
