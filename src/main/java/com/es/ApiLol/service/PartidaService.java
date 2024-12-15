package com.es.ApiLol.service;

import com.es.ApiLol.dto.PartidaDTO;
import com.es.ApiLol.error.exception.ForbiddenException;
import com.es.ApiLol.model.Partida;
import com.es.ApiLol.repository.CampeonRepository;
import com.es.ApiLol.repository.PartidaRepository;
import com.es.ApiLol.repository.UsuarioRepository;
import com.es.ApiLol.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.nio.MappedByteBuffer;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

@Service
public class PartidaService {

    @Autowired
    PartidaRepository partidaRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    CampeonRepository campeonRepository;

    public PartidaDTO create(PartidaDTO partidaDTO, Authentication auth) {

        if (auth.getName().equals(partidaDTO.getUsuario().getUsername())
                || auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {

            Partida partida = Mapper.mapToEntity(partidaDTO);

            partida.setUsuario(usuarioRepository.findByUsername(partidaDTO.getUsuario().getUsername()).orElseThrow(() ->
                    new ForbiddenException("No existe un usuario con el nombre proporcionado.")));

            partida.setCampeon(campeonRepository.findByNombre(partidaDTO.getCampeon().getNombre()).orElseThrow(() ->
                    new ForbiddenException("No existe un campeón con el nombre proporcionado.")));

            partida.setFecha(LocalDateTime.now());

            partida = partidaRepository.save(partida);

            return Mapper.mapToDTO(partida);
        }

        throw new ForbiddenException("No está autorizado a crear esta partida.");
    }


}
