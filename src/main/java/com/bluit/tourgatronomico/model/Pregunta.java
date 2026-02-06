package com.bluit.tourgatronomico.model;

import java.util.List;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonManagedReference; // ✅ AGREGAR

@Entity
@Table(name = "preguntas")
public class Pregunta {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String campo;
    private String titulo;
    private String pregunta;

    @OneToMany(mappedBy = "pregunta", fetch = FetchType.LAZY)
    @JsonManagedReference // ✅ AGREGAR
    private List<Respuesta> respuestas;


/*package com.bluit.tourgatronomico.model;

import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "preguntas")
public class Pregunta {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String campo;      // ✅ AGREGADO - Corresponde a la columna "campo" en la BD
    private String titulo;     // ✅ AGREGADO - Corresponde a la columna "titulo" en la BD
    private String pregunta;   // Ya existía

    @OneToMany(mappedBy = "pregunta", fetch = FetchType.LAZY)
    private List<Respuesta> respuestas;
*/
    // Constructores
    public Pregunta() {}

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCampo() {
        return campo;
    }

    public void setCampo(String campo) {
        this.campo = campo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public List<Respuesta> getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(List<Respuesta> respuestas) {
        this.respuestas = respuestas;
    }
}