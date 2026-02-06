package com.bluit.tourgatronomico.dto;

import java.util.List;
import java.util.Map;

public class LugarCreateDTO {

    private String nombre;
    private String descripcion;
    private String direccion;
    private Double latitud;
    private Double longitud;
    private String telefono;
    private List<Map<String, Object>> horario;
    private Long tipoLugarId;
    private Long usuarioId;
    private List<PlatilloDTO> platillos;

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public Double getLatitud() { return latitud; }
    public void setLatitud(Double latitud) { this.latitud = latitud; }

    public Double getLongitud() { return longitud; }
    public void setLongitud(Double longitud) { this.longitud = longitud; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public List<Map<String, Object>> getHorario() { return horario; }
    public void setHorario(List<Map<String, Object>> horario) { this.horario = horario; }

    public Long getTipoLugarId() { return tipoLugarId; }
    public void setTipoLugarId(Long tipoLugarId) { this.tipoLugarId = tipoLugarId; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public List<PlatilloDTO> getPlatillos() { return platillos; }
    public void setPlatillos(List<PlatilloDTO> platillos) { this.platillos = platillos; }
}
