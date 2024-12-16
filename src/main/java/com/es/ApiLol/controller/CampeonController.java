package com.es.ApiLol.controller;

import com.es.ApiLol.dto.CampeonDTO;
import com.es.ApiLol.service.CampeonService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de campeones en la API de League of Legends.
 * Proporciona endpoints para crear, consultar, actualizar y eliminar campeones.
 */
@RestController
@RequestMapping("/campeones")
public class CampeonController {
    @Autowired
    private CampeonService campeonService;

    /**
     * Crea un nuevo campeón en el sistema.
     *
     * @param campeonDTO Objeto con los datos del campeón a crear.
     * @return ResponseEntity con el campeón creado y el código de estado HTTP 201 (CREATED).
     * @throws IllegalArgumentException Si los datos del campeón son inválidos.
     */
    @PostMapping("/")
    public ResponseEntity<CampeonDTO> create(
            @RequestBody CampeonDTO campeonDTO)
    {
        if (campeonDTO == null || campeonDTO.getNombre().isBlank() || campeonDTO.getTipo().isBlank()) {
            throw new IllegalArgumentException("El campeon no es valido");
        }

        CampeonDTO nuevoCampeon = campeonService.create(campeonDTO);
        return new ResponseEntity<>(nuevoCampeon, HttpStatus.CREATED);
    }

    /**
     * Obtiene una lista de todos los campeones registrados.
     *
     * @return ResponseEntity con la lista de campeones y el código de estado HTTP 200 (OK).
     */
    @GetMapping("/")
    public ResponseEntity<List<CampeonDTO>> getAll() {
        List<CampeonDTO> listaCampeones = campeonService.getAll();
        return new ResponseEntity<>(listaCampeones, HttpStatus.OK);
    }

    /**
     * Obtiene los datos de un campeón específico por su ID.
     *
     * @param id Identificador único del campeón.
     * @return ResponseEntity con los datos del campeón y el código de estado HTTP 200 (OK).
     * @throws IllegalArgumentException Si el ID proporcionado es inválido.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CampeonDTO> getById(
            @PathVariable String id
    ) {

        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El id del campeon no es valido");
        }

        CampeonDTO campeonDTO = campeonService.getById(id);
        return new ResponseEntity<>(campeonDTO, HttpStatus.OK);
    }

    /**
     * Actualiza los datos de un campeón específico por su ID.
     *
     * @param id Identificador único del campeón a actualizar.
     * @param campeonDTO Objeto con los nuevos datos del campeón.
     * @return ResponseEntity con el campeón actualizado y el código de estado HTTP 200 (OK).
     * @throws IllegalArgumentException Si el ID o los datos del campeón son inválidos.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CampeonDTO> updateById(
            @PathVariable String id,
            @RequestBody CampeonDTO campeonDTO
    ) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El id del campeon no es valido");
        }

        if (campeonDTO == null || campeonDTO.getNombre().isBlank() || campeonDTO.getTipo().isBlank()) {
            throw new IllegalArgumentException("El campeon no es valido");
        }

        CampeonDTO campeonActualizado = campeonService.updateById(id, campeonDTO);
        return new ResponseEntity<CampeonDTO>(campeonActualizado, HttpStatus.OK);
    }


    /**
     * Elimina un campeón específico por su ID.
     *
     * @param id Identificador único del campeón a eliminar.
     * @return ResponseEntity con los datos del campeón eliminado y el código de estado HTTP 200 (OK).
     * @throws IllegalArgumentException Si el ID proporcionado es inválido.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<CampeonDTO> deleteById(
            @PathVariable String id
    ) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El id del campeon no es valido");
        }

        CampeonDTO campeonDTO = campeonService.deleteById(id);
        return new ResponseEntity<>(campeonDTO, HttpStatus.OK);
    }
}
