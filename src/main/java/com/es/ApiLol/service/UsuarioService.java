package com.es.ApiLol.service;

import com.es.ApiLol.dto.UsuarioDTO;
import com.es.ApiLol.error.exception.*;
import com.es.ApiLol.model.Usuario;
import com.es.ApiLol.repository.UsuarioRepository;
import com.es.ApiLol.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // BUSCO EL USUARIO POR SU NOMBRE EN LA BDD
        Usuario usuario = usuarioRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario No encontrado"));

        /* RETORNAMOS UN USERDETAILS
        El método loadUserByUsername nos fuerza a devolver un objeto de tipo UserDetails.
        Tenemos que convertir nuestro objeto de tipo Usuario a un objeto de tipo UserDetails
         */
        UserDetails userDetails = User // User pertenece a SpringSecurity
                .builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword())
                .roles(usuario.getRoles().split(","))
                .build();

        return userDetails;
    }

    public UsuarioDTO registerUser(UsuarioDTO usuarioDTO) {
        // Comprobamos que el usuario no existe en la base de datos
        if (usuarioRepository.findByUsername(usuarioDTO.getUsername()).isPresent()) {
            throw new DuplicateException("El nombre de usuario ya existe");
        }
        // Creamos la instancia
        Usuario newUsuario = new Usuario();


        //La password del newUsuario debe estar hasheada, así que usamos el passwordEncoder que tenemos definido.
        newUsuario.setPassword(passwordEncoder.encode(usuarioDTO.getPassword())); // Hashear la contraseña
        newUsuario.setUsername(usuarioDTO.getUsername());
        newUsuario.setRoles(usuarioDTO.getRoles());

        // Guardamos el newUsuario en la base de datos
        usuarioRepository.save(newUsuario);

        return usuarioDTO;
    }

    public List<UsuarioDTO> getAll() {

        List<Usuario> usuarios = usuarioRepository.findAll();

        List<UsuarioDTO> usuarioDTOS = new ArrayList<>();

        usuarios.forEach(usuario -> usuarioDTOS.add(Mapper.mapToDTO(usuario)));

        return usuarioDTOS;
    }


    public UsuarioDTO getById(String id, Authentication auth) {

        Long idL;

        try {
            idL = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Id no válido");
        }

        Usuario usuario = usuarioRepository.findById(idL).orElseThrow(() ->
                new NotFoundException("No existe un usuario con el ID proporcionado."));

        // Comprobamos si el usuario es el mismo que el solicitante o si
        // el solicitante es un administrador
        if (!auth.getName().equals(usuario.getUsername())
                && !auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {

            throw new NotAuthorizedException("No tienes permisos para obtener este usuario.");
        }

        // Si se tiene permiso, devolvemos el DTO del usuario
        return Mapper.mapToDTO(usuario);
    }

    public UsuarioDTO updateById(String id, UsuarioDTO usuarioDTO, Authentication auth) {
        Long idL;

        try {
            idL = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Id no válido");
        }

        if (!auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {

            throw new NotAuthorizedException("No tienes permisos para actualizar este usuario.");
        }
        Usuario usuario = usuarioRepository.findById(idL).orElseThrow(() ->
                new NotFoundException("No existe un usuario con el ID proporcionado."));

        usuario.setUsername(usuarioDTO.getUsername());
        usuario.setPassword(passwordEncoder.encode(usuarioDTO.getPassword()));
        usuario.setRoles(usuarioDTO.getRoles());

        usuarioRepository.save(usuario);

        return Mapper.mapToDTO(usuario);
    }

    public UsuarioDTO deleteById(String id, Authentication auth) {

        Long idL;

        try {
            idL = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Id no válido");
        }

        Usuario usuario = usuarioRepository.findById(idL).orElseThrow(() ->
                new NotFoundException("No existe un usuario con el ID proporcionado."));

        // Comprobamos si el solicitante es administrador
        if (!auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            throw new ForbiddenException("No tienes permisos para eliminar este usuario.");
        }

        // Si el usuario es un administrador, eliminamos el usuario
        usuarioRepository.delete(usuario);

        return Mapper.mapToDTO(usuario);
    }


}
