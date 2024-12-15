package com.es.ApiLol.dto;

import com.es.ApiLol.model.Campeon;
import com.es.ApiLol.model.Usuario;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class PartidaDTO {

    private String resultado;

    private Usuario usuario;

    private Campeon campeon;

    private int duracion;

    public PartidaDTO(String resultado, Usuario usuario, Campeon campeon, int duracion) {

        this.resultado = resultado;
        this.usuario = usuario;
        this.campeon = campeon;
        this.duracion = duracion;
    }

    public PartidaDTO() {}
}
