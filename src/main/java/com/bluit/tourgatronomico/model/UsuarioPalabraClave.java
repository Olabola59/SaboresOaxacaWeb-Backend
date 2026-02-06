package com.bluit.tourgatronomico.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "usuarios_palabras_clave",
       uniqueConstraints = @UniqueConstraint(columnNames = {"usuario_id","palabra_clave_id"}))
public class UsuarioPalabraClave {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false) @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne(optional = false) @JoinColumn(name = "palabra_clave_id")
    private PalabraClave palabra;
}
