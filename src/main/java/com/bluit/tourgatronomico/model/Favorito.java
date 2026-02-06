package com.bluit.tourgatronomico.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "favoritos")
public class Favorito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "platillo_id", nullable = false)
    private Platillo platillo;

    @Column(name = "fecha")
    private LocalDateTime fecha = LocalDateTime.now();

    // Constructores
    public Favorito() {}

    public Favorito(Usuario usuario, Platillo platillo) {
        this.usuario = usuario;
        this.platillo = platillo;
        this.fecha = LocalDateTime.now();
    }

    // Getters y Setters
    public Long getId() { 
        return id; 
    }
    
    public void setId(Long id) { 
        this.id = id; 
    }

    public Usuario getUsuario() { 
        return usuario; 
    }
    
    public void setUsuario(Usuario usuario) { 
        this.usuario = usuario; 
    }

    public Platillo getPlatillo() { 
        return platillo; 
    }
    
    public void setPlatillo(Platillo platillo) { 
        this.platillo = platillo; 
    }

    public LocalDateTime getFecha() { 
        return fecha; 
    }
    
    public void setFecha(LocalDateTime fecha) { 
        this.fecha = fecha; 
    }
}