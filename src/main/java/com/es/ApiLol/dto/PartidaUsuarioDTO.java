package com.es.ApiLol.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartidaUsuarioDTO {
    private String username;

    public PartidaUsuarioDTO(String username) {
        this.username = username;
    }

    public PartidaUsuarioDTO() {}
}
