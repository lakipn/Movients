package com.lazarmilosavljevic.movients.service.mapper;

import com.lazarmilosavljevic.movients.domain.*;
import com.lazarmilosavljevic.movients.service.dto.CastDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Cast and its DTO CastDTO.
 */
@Mapper(componentModel = "spring", uses = {MovieMapper.class})
public interface CastMapper extends EntityMapper<CastDTO, Cast> {

    @Mapping(source = "movie.id", target = "movieId")
    @Mapping(source = "movie.title", target = "movieTitle")
    CastDTO toDto(Cast cast);

    @Mapping(source = "movieId", target = "movie")
    Cast toEntity(CastDTO castDTO);

    default Cast fromId(Long id) {
        if (id == null) {
            return null;
        }
        Cast cast = new Cast();
        cast.setId(id);
        return cast;
    }
}
