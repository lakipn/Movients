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

import com.lazarmilosavljevic.movients.domain.Genre;
import com.lazarmilosavljevic.movients.domain.*; // for static metamodels
import com.lazarmilosavljevic.movients.repository.GenreRepository;
import com.lazarmilosavljevic.movients.service.dto.GenreCriteria;
import com.lazarmilosavljevic.movients.service.dto.GenreDTO;
import com.lazarmilosavljevic.movients.service.mapper.GenreMapper;

/**
 * Service for executing complex queries for Genre entities in the database.
 * The main input is a {@link GenreCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link GenreDTO} or a {@link Page} of {@link GenreDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GenreQueryService extends QueryService<Genre> {

    private final Logger log = LoggerFactory.getLogger(GenreQueryService.class);

    private final GenreRepository genreRepository;

    private final GenreMapper genreMapper;

    public GenreQueryService(GenreRepository genreRepository, GenreMapper genreMapper) {
        this.genreRepository = genreRepository;
        this.genreMapper = genreMapper;
    }

    /**
     * Return a {@link List} of {@link GenreDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<GenreDTO> findByCriteria(GenreCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Genre> specification = createSpecification(criteria);
        return genreMapper.toDto(genreRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link GenreDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GenreDTO> findByCriteria(GenreCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Genre> specification = createSpecification(criteria);
        return genreRepository.findAll(specification, page)
            .map(genreMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GenreCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Genre> specification = createSpecification(criteria);
        return genreRepository.count(specification);
    }

    /**
     * Function to convert GenreCriteria to a {@link Specification}
     */
    private Specification<Genre> createSpecification(GenreCriteria criteria) {
        Specification<Genre> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Genre_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Genre_.name));
            }
            if (criteria.getMoviesId() != null) {
                specification = specification.and(buildSpecification(criteria.getMoviesId(),
                    root -> root.join(Genre_.movies, JoinType.LEFT).get(Movie_.id)));
            }
        }
        return specification;
    }
}
