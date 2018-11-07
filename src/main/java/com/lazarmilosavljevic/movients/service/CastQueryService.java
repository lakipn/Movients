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

import com.lazarmilosavljevic.movients.domain.Cast;
import com.lazarmilosavljevic.movients.domain.*; // for static metamodels
import com.lazarmilosavljevic.movients.repository.CastRepository;
import com.lazarmilosavljevic.movients.service.dto.CastCriteria;
import com.lazarmilosavljevic.movients.service.dto.CastDTO;
import com.lazarmilosavljevic.movients.service.mapper.CastMapper;

/**
 * Service for executing complex queries for Cast entities in the database.
 * The main input is a {@link CastCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CastDTO} or a {@link Page} of {@link CastDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CastQueryService extends QueryService<Cast> {

    private final Logger log = LoggerFactory.getLogger(CastQueryService.class);

    private final CastRepository castRepository;

    private final CastMapper castMapper;

    public CastQueryService(CastRepository castRepository, CastMapper castMapper) {
        this.castRepository = castRepository;
        this.castMapper = castMapper;
    }

    /**
     * Return a {@link List} of {@link CastDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CastDTO> findByCriteria(CastCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Cast> specification = createSpecification(criteria);
        return castMapper.toDto(castRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CastDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CastDTO> findByCriteria(CastCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Cast> specification = createSpecification(criteria);
        return castRepository.findAll(specification, page)
            .map(castMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CastCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Cast> specification = createSpecification(criteria);
        return castRepository.count(specification);
    }

    /**
     * Function to convert CastCriteria to a {@link Specification}
     */
    private Specification<Cast> createSpecification(CastCriteria criteria) {
        Specification<Cast> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Cast_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Cast_.name));
            }
            if (criteria.getCharacterName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCharacterName(), Cast_.characterName));
            }
            if (criteria.getImage() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImage(), Cast_.image));
            }
            if (criteria.getImdb() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImdb(), Cast_.imdb));
            }
            if (criteria.getMovieId() != null) {
                specification = specification.and(buildSpecification(criteria.getMovieId(),
                    root -> root.join(Cast_.movie, JoinType.LEFT).get(Movie_.id)));
            }
        }
        return specification;
    }
}
