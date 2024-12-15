package com.es.ApiLol.controller;

import com.es.ApiLol.dto.CampeonDTO;
import com.es.ApiLol.service.CampeonService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/campeones")
public class CampeonController {
    @Autowired
    private CampeonService campeonService;


    @PostMapping
    public ResponseEntity<CampeonDTO> create(
            @RequestBody CampeonDTO campeonDTO, Authentication auth)
    {
        if (campeonDTO == null || campeonDTO.getNombre().isBlank() || campeonDTO.getTipo().isBlank()) {
            throw new IllegalArgumentException("El campeon no es valido");
        }

        CampeonDTO nuevoCampeon = campeonService.create(campeonDTO, auth);
        return new ResponseEntity<>(nuevoCampeon, HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<List<CampeonDTO>> getAll() {
        List<CampeonDTO> listaCampeones = campeonService.getAll();
        return new ResponseEntity<>(listaCampeones, HttpStatus.OK);
    }


}
