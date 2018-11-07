package com.lazarmilosavljevic.movients.service.mapper;

import com.lazarmilosavljevic.movients.domain.*;
import com.lazarmilosavljevic.movients.service.dto.TorrentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Torrent and its DTO TorrentDTO.
 */
@Mapper(componentModel = "spring", uses = {MovieMapper.class})
public interface TorrentMapper extends EntityMapper<TorrentDTO, Torrent> {

    @Mapping(source = "movie.id", target = "movieId")
    @Mapping(source = "movie.title", target = "movieTitle")
    TorrentDTO toDto(Torrent torrent);

    @Mapping(source = "movieId", target = "movie")
    Torrent toEntity(TorrentDTO torrentDTO);

    default Torrent fromId(Long id) {
        if (id == null) {
            return null;
        }
        Torrent torrent = new Torrent();
        torrent.setId(id);
        return torrent;
    }
}
