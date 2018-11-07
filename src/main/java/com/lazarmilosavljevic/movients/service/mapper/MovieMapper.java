package com.lazarmilosavljevic.movients.service.mapper;

import com.lazarmilosavljevic.movients.domain.*;
import com.lazarmilosavljevic.movients.service.dto.MovieDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Movie and its DTO MovieDTO.
 */
@Mapper(componentModel = "spring", uses = {GenreMapper.class})
public interface MovieMapper extends EntityMapper<MovieDTO, Movie> {


    @Mapping(target = "casts", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "torrents", ignore = true)
    Movie toEntity(MovieDTO movieDTO);

    default Movie fromId(Long id) {
        if (id == null) {
            return null;
        }
        Movie movie = new Movie();
        movie.setId(id);
        return movie;
    }
}
