package com.es.ApiLol.dto;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PartidaCampeonDTO {
    private String nombre;

    public PartidaCampeonDTO(String nombre) {
        this.nombre = nombre;
    }

    public PartidaCampeonDTO() {}
}
