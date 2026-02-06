package com.bluit.tourgatronomico.dto;

import com.bluit.tourgatronomico.model.Platillo;
import java.util.List;

import java.util.Arrays;
import java.util.List;

public class PlatilloDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private Double precio;
    private String historia;
    private String foto;
    private String categoria;
    private List<String> ingredientes; // 👈 AGREGAR
    private Long lugarId;
    private String lugarNombre;
    private String lugarDireccion;
    private String lugarHorario;

    public PlatilloDTO() {}

    public PlatilloDTO(Platillo platillo) {
        this.id = platillo.getId();
        this.nombre = platillo.getNombre();
        this.descripcion = platillo.getDescripcion();
        this.precio = platillo.getPrecio();
        this.historia = platillo.getHistoria();
        this.foto = platillo.getFoto();
        this.categoria = platillo.getCategoria();
        //this.ingredientes = platillo.getIngredientes(); // 👈 AGREGAR
        this.ingredientes = (platillo.getIngredientes() == null)
            ? List.of()
            : Arrays.asList(platillo.getIngredientes());


        if (platillo.getLugar() != null) {
            this.lugarId = platillo.getLugar().getId();
            this.lugarNombre = platillo.getLugar().getNombre();
            this.lugarDireccion = platillo.getLugar().getDireccion();
            this.lugarHorario = platillo.getLugar().getHorario();
        }
    }

    // Getters y Setters
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

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    // 👇 AGREGAR Getter y Setter para ingredientes
    public List<String> getIngredientes() { return ingredientes; }
    public void setIngredientes(List<String> ingredientes) { this.ingredientes = ingredientes; }

    public Long getLugarId() { return lugarId; }
    public void setLugarId(Long lugarId) { this.lugarId = lugarId; }

    public String getLugarNombre() { return lugarNombre; }
    public void setLugarNombre(String lugarNombre) { this.lugarNombre = lugarNombre; }

    public String getLugarDireccion() { return lugarDireccion; }
    public void setLugarDireccion(String lugarDireccion) { this.lugarDireccion = lugarDireccion; }

    public String getLugarHorario() { return lugarHorario; }
    public void setLugarHorario(String lugarHorario) { this.lugarHorario = lugarHorario; }
}