package com.es.ApiLol.service;

import com.es.ApiLol.dto.CampeonDTO;
import com.es.ApiLol.error.exception.BadRequestException;
import com.es.ApiLol.error.exception.DuplicateException;
import com.es.ApiLol.error.exception.NotFoundException;
import com.es.ApiLol.model.Campeon;
import com.es.ApiLol.repository.CampeonRepository;
import com.es.ApiLol.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * Servicio para gestionar las operaciones relacionadas con los campeones en la API de League of Legends.
 * Este servicio incluye creación, consulta, actualización y eliminación de campeones.
 */
@Service
public class CampeonService {

    @Autowired
    private CampeonRepository campeonRepository;

    /**
     * Crea un nuevo campeón y lo guarda en la base de datos.
     *
     * @param campeonDTO Datos del campeón a crear.
     * @return CampeonDTO con los datos del campeón creado.
     * @throws DuplicateException Si ya existe un campeón con el mismo nombre.
     */
    public CampeonDTO create(CampeonDTO campeonDTO) {

        if (campeonRepository.findByNombre(campeonDTO.getNombre()).isPresent()) {
            throw new DuplicateException("El campeón ya existe");
        }

        Campeon nuevoCampeon = new Campeon();
        nuevoCampeon.setNombre(campeonDTO.getNombre());
        nuevoCampeon.setTipo(campeonDTO.getTipo());
        campeonRepository.save(nuevoCampeon);

        return Mapper.mapToDTO(nuevoCampeon);
    }

    /**
     * Obtiene una lista de todos los campeones registrados en la base de datos.
     *
     * @return Lista de CampeonDTO con los datos de los campeones.
     */
    public List<CampeonDTO> getAll() {
        List<Campeon> campeones = campeonRepository.findAll();

        List<CampeonDTO> campeonesDTOS = new ArrayList<>();

        campeones.forEach(campeon -> campeonesDTOS.add(Mapper.mapToDTO(campeon)));

        return campeonesDTOS;
    }

    /**
     * Obtiene los datos de un campeón específico por su ID.
     *
     * @param id Identificador único del campeón.
     * @return CampeonDTO con los datos del campeón encontrado.
     * @throws BadRequestException Si el ID proporcionado no es válido.
     * @throws NotFoundException Si no existe un campeón con el ID proporcionado.
     */
    public CampeonDTO getById(String id) {

        Long idL;

        try {
            idL = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Id no válido");
        }

        Campeon campeon = campeonRepository.findById(idL).orElseThrow(() ->
                new NotFoundException("No existe un campeón con el ID proporcionado."));

        return Mapper.mapToDTO(campeon);
    }

    /**
     * Actualiza los datos de un campeón existente en la base de datos.
     *
     * @param id Identificador único del campeón a actualizar.
     * @param campeonDTO Nuevos datos del campeón.
     * @return CampeonDTO con los datos del campeón actualizado.
     * @throws BadRequestException Si el ID proporcionado no es válido.
     * @throws NotFoundException Si no existe un campeón con el ID proporcionado.
     */
    public CampeonDTO updateById(String id, CampeonDTO campeonDTO) {
        Long idL;

        try {
            idL = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Id no válido");
        }

        Campeon campeon = campeonRepository.findById(idL).orElseThrow(() ->
                new NotFoundException("No existe un campeón con el ID proporcionado."));

        campeon.setNombre(campeonDTO.getNombre());
        campeon.setTipo(campeonDTO.getTipo());
        campeonRepository.save(campeon);

        return Mapper.mapToDTO(campeon);
    }

    /**
     * Elimina un campeón de la base de datos por su ID.
     *
     * @param id Identificador único del campeón a eliminar.
     * @return CampeonDTO con los datos del campeón eliminado.
     * @throws BadRequestException Si el ID proporcionado no es válido.
     * @throws NotFoundException Si no existe un campeón con el ID proporcionado.
     */
    public CampeonDTO deleteById(String id) {

        Long idL;

        try {
            idL = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Id no válido");
        }

        Campeon campeon = campeonRepository.findById(idL).orElseThrow(() ->
                new NotFoundException("No existe un campeón con el ID proporcionado."));

        campeonRepository.delete(campeon);

        return Mapper.mapToDTO(campeon);
    }
}
