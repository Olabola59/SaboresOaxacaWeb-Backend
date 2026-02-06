package com.bluit.tourgatronomico.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "palabras_clave", uniqueConstraints = @UniqueConstraint(columnNames = "nombre"))
public class PalabraClave {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String nombre;
}
