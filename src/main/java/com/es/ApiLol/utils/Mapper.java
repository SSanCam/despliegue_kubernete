package com.es.ApiLol.utils;

import com.es.ApiLol.dto.CampeonDTO;
import com.es.ApiLol.dto.PartidaDTO;
import com.es.ApiLol.dto.UsuarioDTO;
import com.es.ApiLol.model.Campeon;
import com.es.ApiLol.model.Partida;
import com.es.ApiLol.model.Usuario;
import com.es.ApiLol.repository.UsuarioRepository;

/**
 * Clase utilizada para mapear entre entidades del modelo y objetos DTO.
 */
public class Mapper {
    public static UsuarioDTO mapToDTO(Usuario usuario) {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setUsername(usuario.getUsername());
        usuarioDTO.setPassword(usuario.getPassword());
        usuarioDTO.setRoles(usuario.getRoles());
        return usuarioDTO;
    }

    public static Usuario createUsuario(UsuarioDTO usuarioDTO, UsuarioRepository usuarioRepository) {
        Usuario usuario = new Usuario();
        usuario.setUsername(usuarioDTO.getUsername());
        usuario.setPassword(usuarioDTO.getPassword());
        usuario.setRoles(usuarioDTO.getRoles());
        usuarioRepository.save(usuario);
        return usuario;
    }

    public static CampeonDTO mapToDTO(Campeon campeon) {
        CampeonDTO campeonDTO = new CampeonDTO ();
        campeonDTO.setNombre(campeon.getNombre());
        campeonDTO.setTipo(campeon.getTipo());

        return campeonDTO;
    }

    public static PartidaDTO mapToDTO(Partida partida) {
        PartidaDTO partidaDto = new PartidaDTO();
        partidaDto.setResultado(partida.getResultado());
        partidaDto.setUsuario(partida.getUsuario());
        partidaDto.setCampeon(partida.getCampeon());
        partidaDto.setDuracion(partida.getDuracion());
        return partidaDto;
    }

    public static Partida mapToEntity(PartidaDTO partidaDto) {
        Partida partida = new Partida();
        partida.setResultado(partidaDto.getResultado());
        partida.setUsuario(partidaDto.getUsuario());
        partida.setCampeon(partidaDto.getCampeon());
        partida.setDuracion(partidaDto.getDuracion());
        return partida;
    }
}
