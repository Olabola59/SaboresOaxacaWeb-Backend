package com.bluit.tourgatronomico.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BusquedaResponse {
    private String tipo;  // Usuario o Rol
    private Integer id;
    private String nombre;
}
