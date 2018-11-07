package com.lazarmilosavljevic.movients.service;

import com.lazarmilosavljevic.movients.domain.Cast;
import com.lazarmilosavljevic.movients.repository.CastRepository;
import com.lazarmilosavljevic.movients.service.dto.CastDTO;
import com.lazarmilosavljevic.movients.service.mapper.CastMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing Cast.
 */
@Service
@Transactional
public class CastService {

    private final Logger log = LoggerFactory.getLogger(CastService.class);

    private final CastRepository castRepository;

    private final CastMapper castMapper;

    public CastService(CastRepository castRepository, CastMapper castMapper) {
        this.castRepository = castRepository;
        this.castMapper = castMapper;
    }

    /**
     * Save a cast.
     *
     * @param castDTO the entity to save
     * @return the persisted entity
     */
    public CastDTO save(CastDTO castDTO) {
        log.debug("Request to save Cast : {}", castDTO);

        Cast cast = castMapper.toEntity(castDTO);
        cast = castRepository.save(cast);
        return castMapper.toDto(cast);
    }

    /**
     * Get all the casts.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CastDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Casts");
        return castRepository.findAll(pageable)
            .map(castMapper::toDto);
    }


    /**
     * Get one cast by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<CastDTO> findOne(Long id) {
        log.debug("Request to get Cast : {}", id);
        return castRepository.findById(id)
            .map(castMapper::toDto);
    }

    /**
     * Delete the cast by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Cast : {}", id);
        castRepository.deleteById(id);
    }
}
