package com.es.ApiLol.controller;

import com.es.ApiLol.dto.PartidaDTO;
import com.es.ApiLol.service.PartidaService;
import com.es.ApiLol.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de partidas en la API de League of Legends.
 * Proporciona endpoints para crear, consultar, actualizar y eliminar partidas,
 * así como para obtener partidas relacionadas con un campeón específico.
 */
@RestController
@RequestMapping("/partidas")
public class PartidaController {

    @Autowired
    private PartidaService partidaService;


    /**
     * Crea una nueva partida en el sistema.
     *
     * @param partidaDTO Objeto con los datos de la partida a crear.
     * @param auth Información de autenticación del usuario.
     * @return ResponseEntity con la partida creada y el código de estado HTTP 201 (CREATED).
     * @throws IllegalArgumentException Si los datos de la partida son inválidos.
     */
    @PostMapping("/")
    public ResponseEntity<PartidaDTO> create(
            @RequestBody PartidaDTO partidaDTO, Authentication auth
    ) {
        // Validar que los datos de la partida no sean nulos
        Validator.validarPartida(partidaDTO);

        PartidaDTO nuevaPartida = partidaService.create(partidaDTO, auth);
        return new ResponseEntity<>(nuevaPartida, HttpStatus.CREATED);
    }

    /**
     * Obtiene una lista de todas las partidas asociadas al usuario autenticado.
     *
     * @param auth Información de autenticación del usuario.
     * @return ResponseEntity con la lista de partidas y el código de estado HTTP 200 (OK).
     */
    @GetMapping("/")
    public ResponseEntity<List<PartidaDTO>> getPartidas(Authentication auth) {

        List<PartidaDTO> partidas = partidaService.getPartidas(auth);

        return new ResponseEntity<>(partidas, HttpStatus.OK);
    }


    /**
     * Obtiene los datos de una partida específica por su ID.
     *
     * @param id Identificador único de la partida.
     * @return ResponseEntity con los datos de la partida y el código de estado HTTP 200 (OK).
     */
    @GetMapping("/{id}")
    public ResponseEntity<PartidaDTO> getPartidaById(@PathVariable String id) {

        PartidaDTO partida = partidaService.getPartidaById(id);

        return new ResponseEntity<>(partida, HttpStatus.OK);
    }

    /**
     * Elimina una partida específica por su ID.
     *
     * @param id Identificador único de la partida a eliminar.
     * @return ResponseEntity con el código de estado HTTP 204 (NO CONTENT).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            @PathVariable String id
    ) {
        partidaService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Actualiza los datos de una partida específica por su ID.
     *
     * @param id Identificador único de la partida a actualizar.
     * @param partidaDTO Objeto con los nuevos datos de la partida.
     * @return ResponseEntity con la partida actualizada y el código de estado HTTP 200 (OK).
     * @throws IllegalArgumentException Si los datos de la partida son inválidos.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PartidaDTO> updateById(
            @PathVariable String id,
            @RequestBody PartidaDTO partidaDTO
    ) {
        Validator.validarPartida(partidaDTO);

        PartidaDTO updatedPartida = partidaService.updatePartida(id, partidaDTO);
        return new ResponseEntity<>(updatedPartida, HttpStatus.OK);
    }

    /**
     * Obtiene una lista de partidas relacionadas con un campeón específico.
     *
     * @param nombre Nombre del campeón.
     * @param auth Información de autenticación del usuario.
     * @return ResponseEntity con la lista de partidas y el código de estado HTTP 200 (OK).
     */
    @GetMapping("/campeon/{nombre}")
    public ResponseEntity<List<PartidaDTO>> getPartidasByCampeon(
            @PathVariable String nombre, Authentication auth) {

        List<PartidaDTO> partidas = partidaService.getPartidasByCampeon(nombre, auth);
        return new ResponseEntity<>(partidas, HttpStatus.OK);
    }
}
