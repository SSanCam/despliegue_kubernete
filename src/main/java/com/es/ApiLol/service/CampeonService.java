package com.es.ApiLol.service;

import com.es.ApiLol.dto.CampeonDTO;
import com.es.ApiLol.error.exception.BadRequestException;
import com.es.ApiLol.error.exception.DuplicateException;
import com.es.ApiLol.error.exception.ForbiddenException;
import com.es.ApiLol.error.exception.NotFoundException;
import com.es.ApiLol.model.Campeon;
import com.es.ApiLol.repository.CampeonRepository;
import com.es.ApiLol.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CampeonService {

    @Autowired
    private CampeonRepository campeonRepository;


    public CampeonDTO create(CampeonDTO campeonDTO) {

        if (campeonRepository.findByNombre(campeonDTO.getNombre()).isPresent()) {
            throw new DuplicateException("El campeon ya existe");
        }

        Campeon nuevoCampeon = new Campeon();
        nuevoCampeon.setNombre(campeonDTO.getNombre());
        nuevoCampeon.setTipo(campeonDTO.getTipo());
        campeonRepository.save(nuevoCampeon);

        return Mapper.mapToDTO(nuevoCampeon);
    }

    public List<CampeonDTO> getAll() {
        List<Campeon> campeones = campeonRepository.findAll();

        List<CampeonDTO> campeonesDTOS = new ArrayList<>();

        campeones.forEach(campeon -> campeonesDTOS.add(Mapper.mapToDTO(campeon)));

        return campeonesDTOS;

    }


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

    public CampeonDTO updateById(
            String id, CampeonDTO campeonDTO
    ) {

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
