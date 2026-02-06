package com.bluit.tourgatronomico.dto;

public class ResenaRequest {
    private Long usuarioId;
    private Long platilloId;
    private Integer calificacion;
    private String comentario;
    
    // Constructor vacío
    public ResenaRequest() {}
    
    // Constructor con parámetros
    public ResenaRequest(Long usuarioId, Long platilloId, Integer calificacion, String comentario) {
        this.usuarioId = usuarioId;
        this.platilloId = platilloId;
        this.calificacion = calificacion;
        this.comentario = comentario;
    }
    
    // Getters y Setters
    public Long getUsuarioId() {
        return usuarioId;
    }
    
    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }
    
    public Long getPlatilloId() {
        return platilloId;
    }
    
    public void setPlatilloId(Long platilloId) {
        this.platilloId = platilloId;
    }
    
    public Integer getCalificacion() {
        return calificacion;
    }
    
    public void setCalificacion(Integer calificacion) {
        this.calificacion = calificacion;
    }
    
    public String getComentario() {
        return comentario;
    }
    
    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}