package com.lazarmilosavljevic.movients.service;

import com.lazarmilosavljevic.movients.domain.Torrent;
import com.lazarmilosavljevic.movients.repository.TorrentRepository;
import com.lazarmilosavljevic.movients.service.dto.TorrentDTO;
import com.lazarmilosavljevic.movients.service.mapper.TorrentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing Torrent.
 */
@Service
@Transactional
public class TorrentService {

    private final Logger log = LoggerFactory.getLogger(TorrentService.class);

    private final TorrentRepository torrentRepository;

    private final TorrentMapper torrentMapper;

    public TorrentService(TorrentRepository torrentRepository, TorrentMapper torrentMapper) {
        this.torrentRepository = torrentRepository;
        this.torrentMapper = torrentMapper;
    }

    /**
     * Save a torrent.
     *
     * @param torrentDTO the entity to save
     * @return the persisted entity
     */
    public TorrentDTO save(TorrentDTO torrentDTO) {
        log.debug("Request to save Torrent : {}", torrentDTO);

        Torrent torrent = torrentMapper.toEntity(torrentDTO);
        torrent = torrentRepository.save(torrent);
        return torrentMapper.toDto(torrent);
    }

    /**
     * Get all the torrents.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TorrentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Torrents");
        return torrentRepository.findAll(pageable)
            .map(torrentMapper::toDto);
    }


    /**
     * Get one torrent by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<TorrentDTO> findOne(Long id) {
        log.debug("Request to get Torrent : {}", id);
        return torrentRepository.findById(id)
            .map(torrentMapper::toDto);
    }

    /**
     * Delete the torrent by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Torrent : {}", id);
        torrentRepository.deleteById(id);
    }
}
