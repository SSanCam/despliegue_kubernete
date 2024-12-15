package com.es.ApiLol.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CampeonDTO {
    private String nombre;
    private String tipo;

    public CampeonDTO(String nombre, String tipo) {
        this.nombre = nombre;
        this.tipo = tipo;
    }
    public CampeonDTO() {}
}
