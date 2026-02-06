package com.bluit.tourgatronomico.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference; // ✅ AGREGAR

@Entity
@Table(name = "respuestas")
public class Respuesta {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pregunta_id")
    @JsonBackReference // ✅ AGREGAR
    private Pregunta pregunta;

    private String valor;
    private String descripcion;



/*package com.bluit.tourgatronomico.model;

import jakarta.persistence.*;

@Entity
@Table(name = "respuestas")
public class Respuesta {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pregunta_id")
    private Pregunta pregunta;

    private String valor;         // ✅ Cambio de "respuesta" a "valor"
    private String descripcion;   // ✅ Agregado
*/
    // Constructores
    public Respuesta() {}

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pregunta getPregunta() {
        return pregunta;
    }

    public void setPregunta(Pregunta pregunta) {
        this.pregunta = pregunta;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}