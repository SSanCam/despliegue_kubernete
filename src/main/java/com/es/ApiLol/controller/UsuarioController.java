package com.es.ApiLol.controller;

import com.es.ApiLol.dto.UsuarioDTO;
import com.es.ApiLol.error.exception.BadRequestException;
import com.es.ApiLol.error.exception.GenericInternalException;
import com.es.ApiLol.error.exception.NotFoundException;
import com.es.ApiLol.service.TokenService;
import com.es.ApiLol.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TokenService tokenService;


    @PostMapping("/login")
    public String login(
            @RequestBody UsuarioDTO usuarioDTO
    ) {
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(usuarioDTO.getUsername(), usuarioDTO.getPassword())// modo de autenticación
            );
        } catch (Exception e) {
            throw new NotFoundException("Excepcion en authentication");
        }

        try {
            return tokenService.generateToken(authentication);
        } catch (Exception e) {
            throw new GenericInternalException("Error al generar el token");
        }
    }


    @PostMapping("/register")
    public ResponseEntity<UsuarioDTO> register(
            @RequestBody UsuarioDTO usuarioRegisterDTO
    ) {
        if (usuarioRegisterDTO == null || usuarioRegisterDTO.getPassword().isBlank() || usuarioRegisterDTO.getUsername().isBlank()) {
            throw new BadRequestException("El usuario no es valido");
        }

        UsuarioDTO usuarioNuevo = usuarioService.registerUser(usuarioRegisterDTO);

        return new ResponseEntity<UsuarioDTO>(usuarioRegisterDTO, HttpStatus.OK);

    }

    @GetMapping("/")
    public ResponseEntity<List<UsuarioDTO>> getAll() {

        List<UsuarioDTO> usuarios = usuarioService.getAll();

        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> getById(
            @PathVariable String id, Authentication auth
    ) {
        if (id == null || id.isBlank()) {
            throw new BadRequestException("El id del usuario no es valido");
        }

        UsuarioDTO usuario = usuarioService.getById(id, auth);

        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> updateById(
            @PathVariable String id,
            @RequestBody UsuarioDTO usuarioDTO, Authentication auth
    ) {
        if (id == null || id.isBlank()) {
            throw new BadRequestException("El id del usuario no es valido");
        }

        if (usuarioDTO == null || usuarioDTO.getPassword().isBlank() || usuarioDTO.getUsername().isBlank()) {
            throw new BadRequestException("El usuario no es valido");
        }

        UsuarioDTO usuarioActualizado = usuarioService.updateById(id, usuarioDTO, auth);
        return new ResponseEntity<>(usuarioActualizado, HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<UsuarioDTO> deleteById(@PathVariable String id, Authentication auth) {
        if (id == null || id.isBlank()) {
            throw new BadRequestException("El id del usuario no es valido");
        }

        UsuarioDTO usuarioDTO = usuarioService.deleteById(id, auth);

        return new ResponseEntity<>(usuarioDTO, HttpStatus.OK);
    }
}
