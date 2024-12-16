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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PartidaService {

    @Autowired
    private PartidaRepository partidaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CampeonRepository campeonRepository;

    /**
     * Crea una nueva partida en la base de datos.
     *
     * @param partidaDTO Información de la partida a crear.
     * @param auth Información de autenticación del usuario actual.
     * @return DTO de la partida creada.
     * @throws ForbiddenException si el usuario no está autorizado.
     */
    public PartidaDTO create(PartidaDTO partidaDTO, Authentication auth) {

        // Validar que el usuario autenticado sea dueño de la partida o un administrador.
        if (auth.getName().equals(partidaDTO.getUsuario().getUsername())
                || auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {

            Partida partida = Mapper.mapToEntity(partidaDTO);

            // Validar la existencia del usuario asociado.
            partida.setUsuario(usuarioRepository.findByUsername(partidaDTO.getUsuario().getUsername()).orElseThrow(() ->
                    new ForbiddenException("No existe un usuario con el nombre proporcionado.")));

            // Validar la existencia del campeón asociado.
            partida.setCampeon(campeonRepository.findByNombre(partidaDTO.getCampeon().getNombre()).orElseThrow(() ->
                    new ForbiddenException("No existe un campeón con el nombre proporcionado.")));

            // Establecer la fecha actual como fecha de la partida.
            partida.setFecha(LocalDateTime.now());

            partida = partidaRepository.save(partida);

            return Mapper.mapToDTO(partida);
        }

        throw new ForbiddenException("No está autorizado a crear esta partida.");
    }

    /**
     * Obtiene todas las partidas disponibles, filtradas según el rol del usuario.
     *
     * @param auth Información de autenticación del usuario actual.
     * @return Lista de partidas en formato DTO.
     */
    public List<PartidaDTO> getPartidas(Authentication auth) {
        List<Partida> partidas = partidaRepository.findAll();

        List<PartidaDTO> partidaDTOS = new ArrayList<>();

        // Administradores ven todas las partidas.
        if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            partidas.forEach(partida -> partidaDTOS.add(Mapper.mapToDTO(partida)));

        } else {
            // Filtrar partidas del usuario autenticado.
            partidas.forEach(partida -> {
                if (partida.getUsuario().getUsername().equals(auth.getName())) {
                    partidaDTOS.add(Mapper.mapToDTO(partida));
                }
            });
        }
        return partidaDTOS;
    }

    /**
     * Obtiene una partida por su ID.
     *
     * @param id ID de la partida a buscar.
     * @return DTO de la partida encontrada.
     * @throws BadRequestException si el ID es inválido.
     * @throws NotFoundException si la partida no existe.
     */
    public PartidaDTO getPartidaById(String id) {
        Long idL;

        // Validar que el ID sea numérico.
        try {
            idL = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Id no válido");
        }

        // Buscar la partida en el repositorio.
        Partida partida = partidaRepository.findById(idL).orElseThrow(() ->
                new NotFoundException("No existe una partida con el ID proporcionado."));

        return Mapper.mapToDTO(partida);
    }

    /**
     * Elimina una partida por su ID.
     *
     * @param id ID de la partida a eliminar.
     * @throws BadRequestException si el ID es inválido.
     * @throws NotFoundException si la partida no existe.
     */
    public void deleteById(String id) {
        Long idL;

        // Validar que el ID sea numérico.
        try {
            idL = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Id no válido");
        }

        // Buscar la partida en el repositorio.
        Partida partida = partidaRepository.findById(idL).orElseThrow(() ->
                new NotFoundException("No existe una partida con el ID proporcionado."));
        partidaRepository.delete(partida);
    }

    /**
     * Actualiza una partida existente por su ID.
     *
     * @param id ID de la partida a actualizar.
     * @param partidaDTO Datos actualizados de la partida.
     * @return DTO de la partida actualizada.
     * @throws BadRequestException si el ID o los datos son inválidos.
     * @throws NotFoundException si la partida no existe.
     */
    public PartidaDTO updatePartida(String id, PartidaDTO partidaDTO) {

        Long idL;

        try {
            idL = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Id no válido");
        }

        // Buscar la partida en el repositorio.
        Partida partida = partidaRepository.findById(idL).orElseThrow(() ->
                new NotFoundException("No existe una partida con el ID proporcionado."));

        // Actualizar datos.
        partida.setFecha(LocalDateTime.now());
        partida.setResultado(partidaDTO.getResultado());
        partida.setDuracion(partidaDTO.getDuracion());

        // Validar la existencia del campeón y usuario asociados.
        partida.setCampeon(campeonRepository.findByNombre(partidaDTO.getCampeon().getNombre()).orElseThrow(() ->
                new BadRequestException("No existe un campeón con el nombre proporcionado.")));
        partida.setUsuario(usuarioRepository.findByUsername(partidaDTO.getUsuario().getUsername()).orElseThrow(() ->
                new BadRequestException("No existe un usuario con el username proporcionado.")));

        partida = partidaRepository.save(partida);

        return Mapper.mapToDTO(partida);
    }

    /**
     * Obtiene una lista de partidas relacionadas con un campeón específico.
     *
     * @param nombre Nombre del campeón.
     * @param auth Información de autenticación del usuario actual.
     * @return Lista de partidas asociadas al campeón en formato DTO.
     */
    public List<PartidaDTO> getPartidasByCampeon(String nombre, Authentication auth) {
        List<Partida> partidas = partidaRepository.findAll();
        List<PartidaDTO> partidaDTOS = new ArrayList<>();

        // Valida si el usuario tiene rol de administrador
        if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
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
