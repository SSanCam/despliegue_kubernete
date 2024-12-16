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

/**
 * Controlador REST para la gestión de usuarios en la API de League of Legends.
 * Proporciona endpoints para registro, inicio de sesión y manejo de usuarios (CRUD).
 */
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TokenService tokenService;

    /**
     * Inicia sesión de un usuario y genera un token JWT.
     *
     * @param usuarioDTO Datos del usuario (nombre de usuario y contraseña).
     * @return Token JWT generado.
     * @throws NotFoundException Si hay un error durante la autenticación.
     * @throws GenericInternalException Si ocurre un error al generar el token.
     */
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

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @param usuarioRegisterDTO Datos del usuario a registrar.
     * @return ResponseEntity con el usuario registrado y el código de estado HTTP 200 (OK).
     * @throws BadRequestException Si los datos del usuario son inválidos.
     */
    @PostMapping("/register")
    public ResponseEntity<UsuarioDTO> register(
            @RequestBody UsuarioDTO usuarioRegisterDTO
    ) {
        if (usuarioRegisterDTO == null || usuarioRegisterDTO.getPassword().isBlank() || usuarioRegisterDTO.getUsername().isBlank()) {
            throw new BadRequestException("El usuario no es valido");
        }

        UsuarioDTO usuarioNuevo = usuarioService.registerUser(usuarioRegisterDTO);

        return new ResponseEntity<>(usuarioNuevo, HttpStatus.OK);

    }

    /**
     * Obtiene una lista de todos los usuarios registrados.
     *
     * @return ResponseEntity con la lista de usuarios y el código de estado HTTP 200 (OK).
     */
    @GetMapping("/")
    public ResponseEntity<List<UsuarioDTO>> getAll() {

        List<UsuarioDTO> usuarios = usuarioService.getAll();

        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    /**
     * Obtiene los datos de un usuario específico por su ID.
     *
     * @param id Identificador único del usuario.
     * @param auth Información de autenticación del usuario.
     * @return ResponseEntity con los datos del usuario y el código de estado HTTP 200 (OK).
     * @throws BadRequestException Si el ID del usuario es inválido.
     */
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

    /**
     * Actualiza los datos de un usuario específico por su ID.
     *
     * @param id Identificador único del usuario a actualizar.
     * @param usuarioDTO Datos nuevos del usuario.
     * @return ResponseEntity con el usuario actualizado y el código de estado HTTP 200 (OK).
     * @throws BadRequestException Si el ID o los datos del usuario son inválidos.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> updateById(
            @PathVariable String id,
            @RequestBody UsuarioDTO usuarioDTO
    ) {
        if (id == null || id.isBlank()) {
            throw new BadRequestException("El id del usuario no es valido");
        }

        if (usuarioDTO == null || usuarioDTO.getPassword().isBlank() || usuarioDTO.getUsername().isBlank()) {
            throw new BadRequestException("El usuario no es valido");
        }

        UsuarioDTO usuarioActualizado = usuarioService.updateById(id, usuarioDTO);
        return new ResponseEntity<>(usuarioActualizado, HttpStatus.OK);
    }

    /**
     * Elimina un usuario específico por su ID.
     *
     * @param id Identificador único del usuario a eliminar.
     * @return ResponseEntity con el usuario eliminado y el código de estado HTTP 200 (OK).
     * @throws BadRequestException Si el ID del usuario es inválido.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<UsuarioDTO> deleteById(@PathVariable String id) {
        if (id == null || id.isBlank()) {
            throw new BadRequestException("El id del usuario no es valido");
        }

        UsuarioDTO usuarioDTO = usuarioService.deleteById(id);

        return new ResponseEntity<>(usuarioDTO, HttpStatus.OK);
    }
}
