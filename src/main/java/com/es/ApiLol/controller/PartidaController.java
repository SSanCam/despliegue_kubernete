package com.es.ApiLol.controller;

import com.es.ApiLol.dto.PartidaDTO;
import com.es.ApiLol.service.PartidaService;
import com.es.ApiLol.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/partidas")
public class PartidaController {

    @Autowired
    private PartidaService partidaService;

    @PostMapping("/")
    public ResponseEntity<PartidaDTO> create(
            @RequestBody PartidaDTO partidaDTO, Authentication auth
    ) {
        // Validar la paritida
        Validator.validarPartida(partidaDTO);



        PartidaDTO nuevaPartida = partidaService.create(partidaDTO, auth);
        return new ResponseEntity<>(nuevaPartida, HttpStatus.CREATED);
    }
}
