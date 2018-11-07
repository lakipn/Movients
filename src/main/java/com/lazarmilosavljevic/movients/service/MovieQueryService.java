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

import com.lazarmilosavljevic.movients.domain.Movie;
import com.lazarmilosavljevic.movients.domain.*; // for static metamodels
import com.lazarmilosavljevic.movients.repository.MovieRepository;
import com.lazarmilosavljevic.movients.service.dto.MovieCriteria;
import com.lazarmilosavljevic.movients.service.dto.MovieDTO;
import com.lazarmilosavljevic.movients.service.mapper.MovieMapper;

/**
 * Service for executing complex queries for Movie entities in the database.
 * The main input is a {@link MovieCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MovieDTO} or a {@link Page} of {@link MovieDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MovieQueryService extends QueryService<Movie> {

    private final Logger log = LoggerFactory.getLogger(MovieQueryService.class);

    private final MovieRepository movieRepository;

    private final MovieMapper movieMapper;

    public MovieQueryService(MovieRepository movieRepository, MovieMapper movieMapper) {
        this.movieRepository = movieRepository;
        this.movieMapper = movieMapper;
    }

    /**
     * Return a {@link List} of {@link MovieDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MovieDTO> findByCriteria(MovieCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Movie> specification = createSpecification(criteria);
        return movieMapper.toDto(movieRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MovieDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MovieDTO> findByCriteria(MovieCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Movie> specification = createSpecification(criteria);
        return movieRepository.findAll(specification, page)
            .map(movieMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MovieCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Movie> specification = createSpecification(criteria);
        return movieRepository.count(specification);
    }

    /**
     * Function to convert MovieCriteria to a {@link Specification}
     */
    private Specification<Movie> createSpecification(MovieCriteria criteria) {
        Specification<Movie> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Movie_.id));
            }
            if (criteria.getImdbCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImdbCode(), Movie_.imdbCode));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Movie_.title));
            }
            if (criteria.getSlug() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSlug(), Movie_.slug));
            }
            if (criteria.getYear() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getYear(), Movie_.year));
            }
            if (criteria.getRating() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRating(), Movie_.rating));
            }
            if (criteria.getRuntime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRuntime(), Movie_.runtime));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Movie_.description));
            }
            if (criteria.getYoutube() != null) {
                specification = specification.and(buildStringSpecification(criteria.getYoutube(), Movie_.youtube));
            }
            if (criteria.getLanguage() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLanguage(), Movie_.language));
            }
            if (criteria.getCastId() != null) {
                specification = specification.and(buildSpecification(criteria.getCastId(),
                    root -> root.join(Movie_.casts, JoinType.LEFT).get(Cast_.id)));
            }
            if (criteria.getImagesId() != null) {
                specification = specification.and(buildSpecification(criteria.getImagesId(),
                    root -> root.join(Movie_.images, JoinType.LEFT).get(Image_.id)));
            }
            if (criteria.getTorrentsId() != null) {
                specification = specification.and(buildSpecification(criteria.getTorrentsId(),
                    root -> root.join(Movie_.torrents, JoinType.LEFT).get(Torrent_.id)));
            }
            if (criteria.getGenresId() != null) {
                specification = specification.and(buildSpecification(criteria.getGenresId(),
                    root -> root.join(Movie_.genres, JoinType.LEFT).get(Genre_.id)));
            }
        }
        return specification;
    }
}
