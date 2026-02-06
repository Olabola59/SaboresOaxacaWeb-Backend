package com.bluit.tourgatronomico.repository;

import com.bluit.tourgatronomico.model.ResenaLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ResenaLikeRepository extends JpaRepository<ResenaLike, Long> {

    long countByResenaId(Long resenaId);

    boolean existsByResenaIdAndUsuarioId(Long resenaId, Long usuarioId);

    void deleteByResenaIdAndUsuarioId(Long resenaId, Long usuarioId);

    // Total likes recibidos por las reseñas de un usuario
    @Query("""
        SELECT COUNT(rl.id)
        FROM ResenaLike rl
        WHERE rl.resenaId IN (
            SELECT r.id FROM Resena r WHERE r.usuarioId = :usuarioId
        )
    """)
    long countLikesRecibidosByUsuario(Long usuarioId);
}
