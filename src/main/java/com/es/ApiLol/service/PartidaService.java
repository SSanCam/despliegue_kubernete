package com.es.ApiLol.service;

import com.es.ApiLol.dto.PartidaDTO;
import com.es.ApiLol.error.exception.BadRequestException;
import com.es.ApiLol.error.exception.ForbiddenException;
import com.es.ApiLol.error.exception.NotFoundException;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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


    public List<PartidaDTO> getPartidas(Authentication auth) {
        List<Partida> partidas = partidaRepository.findAll();

        List<PartidaDTO> partidaDTOS = new ArrayList<>();

        if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            partidas.forEach(partida -> partidaDTOS.add(Mapper.mapToDTO(partida)));
            return partidaDTOS;
        } else {
            partidas.forEach(partida -> {
                if (partida.getUsuario().getUsername().equals(auth.getName())) {
                    partidaDTOS.add(Mapper.mapToDTO(partida));
                }
            });
            return partidaDTOS;
        }
    }

    public PartidaDTO getPartidaById(String id) {
        Long idL;

        try {
            idL = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Id no válido");
        }

        Partida partida = partidaRepository.findById(idL).orElseThrow(() ->
                new BadRequestException("No existe una partida con el ID proporcionado."));

        return Mapper.mapToDTO(partida);
    }

    public void deleteById(String id) {
        Long idL;

        try {
            idL = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Id no válido");
        }

        Partida partida = partidaRepository.findById(idL).orElseThrow(() ->
                new NotFoundException("No existe una partida con el ID proporcionado."));
        partidaRepository.delete(partida);
    }

    public PartidaDTO updatePartida(String id, PartidaDTO partidaDTO) {

        Long idL;

        try {
            idL = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Id no válido");
        }

        Partida partida = partidaRepository.findById(idL).orElseThrow(() ->
                new NotFoundException("No existe una partida con el ID proporcionado."));

        partida.setFecha(LocalDateTime.now());

        partida.setResultado(partidaDTO.getResultado());

        partida.setDuracion(partidaDTO.getDuracion());

        partida.setCampeon(campeonRepository.findByNombre(partidaDTO.getCampeon().getNombre()).orElseThrow(() ->
                new BadRequestException("No existe un campeón con el nombre proporcionado.")));

        partida.setUsuario(usuarioRepository.findByUsername(partidaDTO.getUsuario().getUsername()).orElseThrow(() ->
                new BadRequestException("No existe un usuario con el username proporcionado.")));

        partida = partidaRepository.save(partida);

        return Mapper.mapToDTO(partida);
    }


    public List<PartidaDTO> getPartidasByCampeon(String nombre, Authentication auth) {
        List<Partida> partidas = partidaRepository.findAll();
        List<PartidaDTO> partidaDTOS = new ArrayList<>();

        // Valida si el usuario tiene rol de administrador
        if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            // Si es administrador, devuelve todas las partidas con el campeón solicitado
            partidas.forEach(partida -> {
                if (partida.getCampeon().getNombre().equalsIgnoreCase(nombre)) {
                    partidaDTOS.add(Mapper.mapToDTO(partida));
                }
            });
        } else {
            // Si no es administrador, filtra por el usuario actual y el campeón solicitado
            partidas.forEach(partida -> {
                if (partida.getCampeon().getNombre().equalsIgnoreCase(nombre)
                        && partida.getUsuario().getUsername().equals(auth.getName())) {
                    partidaDTOS.add(Mapper.mapToDTO(partida));
                }
            });
        }

        return partidaDTOS;
    }
}
