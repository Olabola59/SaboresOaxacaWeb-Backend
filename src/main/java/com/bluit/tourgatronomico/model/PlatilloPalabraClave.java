package com.bluit.tourgatronomico.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(
  name = "platillos_palabras_clave",
  uniqueConstraints = @UniqueConstraint(columnNames = {"platillo_id","palabra_clave_id"})
)
public class PlatilloPalabraClave {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "platillo_id")
  private Platillo platillo;

  @ManyToOne(optional = false)
  @JoinColumn(name = "palabra_clave_id")
  private PalabraClave palabra;
}
