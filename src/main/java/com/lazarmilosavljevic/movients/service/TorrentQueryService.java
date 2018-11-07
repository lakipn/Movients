package com.lazarmilosavljevic.movients.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.lazarmilosavljevic.movients.domain.Torrent;
import com.lazarmilosavljevic.movients.domain.*; // for static metamodels
import com.lazarmilosavljevic.movients.repository.TorrentRepository;
import com.lazarmilosavljevic.movients.service.dto.TorrentCriteria;
import com.lazarmilosavljevic.movients.service.dto.TorrentDTO;
import com.lazarmilosavljevic.movients.service.mapper.TorrentMapper;

/**
 * Service for executing complex queries for Torrent entities in the database.
 * The main input is a {@link TorrentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TorrentDTO} or a {@link Page} of {@link TorrentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TorrentQueryService extends QueryService<Torrent> {

    private final Logger log = LoggerFactory.getLogger(TorrentQueryService.class);

    private final TorrentRepository torrentRepository;

    private final TorrentMapper torrentMapper;

    public TorrentQueryService(TorrentRepository torrentRepository, TorrentMapper torrentMapper) {
        this.torrentRepository = torrentRepository;
        this.torrentMapper = torrentMapper;
    }

    /**
     * Return a {@link List} of {@link TorrentDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TorrentDTO> findByCriteria(TorrentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Torrent> specification = createSpecification(criteria);
        return torrentMapper.toDto(torrentRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TorrentDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TorrentDTO> findByCriteria(TorrentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Torrent> specification = createSpecification(criteria);
        return torrentRepository.findAll(specification, page)
            .map(torrentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TorrentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Torrent> specification = createSpecification(criteria);
        return torrentRepository.count(specification);
    }

    /**
     * Function to convert TorrentCriteria to a {@link Specification}
     */
    private Specification<Torrent> createSpecification(TorrentCriteria criteria) {
        Specification<Torrent> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Torrent_.id));
            }
            if (criteria.getUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrl(), Torrent_.url));
            }
            if (criteria.getHash() != null) {
                specification = specification.and(buildStringSpecification(criteria.getHash(), Torrent_.hash));
            }
            if (criteria.getQuality() != null) {
                specification = specification.and(buildStringSpecification(criteria.getQuality(), Torrent_.quality));
            }
            if (criteria.getSize() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSize(), Torrent_.size));
            }
            if (criteria.getMovieId() != null) {
                specification = specification.and(buildSpecification(criteria.getMovieId(),
                    root -> root.join(Torrent_.movie, JoinType.LEFT).get(Movie_.id)));
            }
        }
        return specification;
    }
}
