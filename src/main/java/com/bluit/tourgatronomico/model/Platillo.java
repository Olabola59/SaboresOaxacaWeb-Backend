package com.bluit.tourgatronomico.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "platillos")
public class Platillo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;

    // En BD es numeric. Mejor BigDecimal (recomendado), pero Double puede funcionar si está bien.
    private Double precio;

    private String historia;
    private String foto;

   @Column(name="ingredientes", columnDefinition="text[]")
    private String[] ingredientes;

    @Column(name="categoria")
    private String categoria;

    @ManyToOne
    @JoinColumn(name = "lugar_id")
    @JsonIgnoreProperties({"platillos", "fotos", "tipoLugar"})
    private Lugar lugar;

    @Enumerated(EnumType.STRING)
    @Column(name = "momento_dia")
    private MomentoDia momentoDia;


    // ========= GETTERS & SETTERS =========
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }

    public String getHistoria() { return historia; }
    public void setHistoria(String historia) { this.historia = historia; }

    public String getFoto() { return foto; }
    public void setFoto(String foto) { this.foto = foto; }

    public Lugar getLugar() { return lugar; }
    public void setLugar(Lugar lugar) { this.lugar = lugar; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String[] getIngredientes() { return ingredientes; }
    public void setIngredientes(String[] ingredientes) { this.ingredientes = ingredientes; }

    public MomentoDia getMomentoDia() { return momentoDia; }
    public void setMomentoDia(MomentoDia momentoDia) { this.momentoDia = momentoDia; }


}
