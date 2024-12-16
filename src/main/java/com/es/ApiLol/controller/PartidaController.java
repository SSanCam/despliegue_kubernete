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

@RestController
@RequestMapping("/partidas")
public class PartidaController {

    @Autowired
    private PartidaService partidaService;

    @PostMapping("/")
    public ResponseEntity<PartidaDTO> create(
            @RequestBody PartidaDTO partidaDTO, Authentication auth
    ) {
        // Validar que los datos de la partida no sean nulos
        Validator.validarPartida(partidaDTO);

        PartidaDTO nuevaPartida = partidaService.create(partidaDTO, auth);
        return new ResponseEntity<>(nuevaPartida, HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<PartidaDTO>> getPartidas(Authentication auth) {

        List<PartidaDTO> partidas = partidaService.getPartidas(auth);

        return new ResponseEntity<>(partidas, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PartidaDTO> getPartidaById(@PathVariable String id) {

        PartidaDTO partida = partidaService.getPartidaById(id);

        return new ResponseEntity<>(partida, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            @PathVariable String id
    ) {
        partidaService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PartidaDTO> updateById(
            @PathVariable String id,
            @RequestBody PartidaDTO partidaDTO
    ) {
        Validator.validarPartida(partidaDTO);

        PartidaDTO updatedPartida = partidaService.updatePartida(id, partidaDTO);
        return new ResponseEntity<>(updatedPartida, HttpStatus.OK);
    }

    @GetMapping("/campeon/{nombre}")
    public ResponseEntity<List<PartidaDTO>> getPartidasByCampeon(
            @PathVariable String nombre, Authentication auth) {

        List<PartidaDTO> partidas = partidaService.getPartidasByCampeon(nombre, auth);
        return new ResponseEntity<>(partidas, HttpStatus.OK);
    }
}
