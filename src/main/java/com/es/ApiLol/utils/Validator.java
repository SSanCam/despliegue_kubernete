package com.es.ApiLol.utils;

import com.es.ApiLol.dto.PartidaDTO;
import com.es.ApiLol.error.exception.BadRequestException;

import java.util.Date;

public class Validator {

    public static boolean validarPartida(PartidaDTO partidaDto) {


        // Validar el resultado
        if (partidaDto.getResultado() == null || partidaDto.getResultado().trim().isEmpty()) {
            throw new BadRequestException("El resultado no puede estar vacío");
        }

        // Validar el usuario
        if (partidaDto.getUsuario() == null) {
            throw new BadRequestException("El usuario no puede estar vacío.");
        }

        // Validar el campeón
        if (partidaDto.getCampeon() == null) {
            throw new BadRequestException("El campeón no puede estar vacío.");
        }

        // Validar la duración
        if (partidaDto.getDuracion() <= 0) {
            throw new BadRequestException("La duración debe ser mayor a 0.");
        }

        return true;
    }
}
