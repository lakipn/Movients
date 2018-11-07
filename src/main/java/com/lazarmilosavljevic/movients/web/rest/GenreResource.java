package com.lazarmilosavljevic.movients.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lazarmilosavljevic.movients.service.GenreService;
import com.lazarmilosavljevic.movients.web.rest.errors.BadRequestAlertException;
import com.lazarmilosavljevic.movients.web.rest.util.HeaderUtil;
import com.lazarmilosavljevic.movients.web.rest.util.PaginationUtil;
import com.lazarmilosavljevic.movients.service.dto.GenreDTO;
import com.lazarmilosavljevic.movients.service.dto.GenreCriteria;
import com.lazarmilosavljevic.movients.service.GenreQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Genre.
 */
@RestController
@RequestMapping("/api")
public class GenreResource {

    private final Logger log = LoggerFactory.getLogger(GenreResource.class);

    private static final String ENTITY_NAME = "genre";

    private final GenreService genreService;

    private final GenreQueryService genreQueryService;

    public GenreResource(GenreService genreService, GenreQueryService genreQueryService) {
        this.genreService = genreService;
        this.genreQueryService = genreQueryService;
    }

    /**
     * POST  /genres : Create a new genre.
     *
     * @param genreDTO the genreDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new genreDTO, or with status 400 (Bad Request) if the genre has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/genres")
    @Timed
    public ResponseEntity<GenreDTO> createGenre(@RequestBody GenreDTO genreDTO) throws URISyntaxException {
        log.debug("REST request to save Genre : {}", genreDTO);
        if (genreDTO.getId() != null) {
            throw new BadRequestAlertException("A new genre cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GenreDTO result = genreService.save(genreDTO);
        return ResponseEntity.created(new URI("/api/genres/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /genres : Updates an existing genre.
     *
     * @param genreDTO the genreDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated genreDTO,
     * or with status 400 (Bad Request) if the genreDTO is not valid,
     * or with status 500 (Internal Server Error) if the genreDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/genres")
    @Timed
    public ResponseEntity<GenreDTO> updateGenre(@RequestBody GenreDTO genreDTO) throws URISyntaxException {
        log.debug("REST request to update Genre : {}", genreDTO);
        if (genreDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        GenreDTO result = genreService.save(genreDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, genreDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /genres : get all the genres.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of genres in body
     */
    @GetMapping("/genres")
    @Timed
    public ResponseEntity<List<GenreDTO>> getAllGenres(GenreCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Genres by criteria: {}", criteria);
        Page<GenreDTO> page = genreQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/genres");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /genres/count : count all the genres.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/genres/count")
    @Timed
    public ResponseEntity<Long> countGenres(GenreCriteria criteria) {
        log.debug("REST request to count Genres by criteria: {}", criteria);
        return ResponseEntity.ok().body(genreQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /genres/:id : get the "id" genre.
     *
     * @param id the id of the genreDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the genreDTO, or with status 404 (Not Found)
     */
    @GetMapping("/genres/{id}")
    @Timed
    public ResponseEntity<GenreDTO> getGenre(@PathVariable Long id) {
        log.debug("REST request to get Genre : {}", id);
        Optional<GenreDTO> genreDTO = genreService.findOne(id);
        return ResponseUtil.wrapOrNotFound(genreDTO);
    }

    /**
     * DELETE  /genres/:id : delete the "id" genre.
     *
     * @param id the id of the genreDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/genres/{id}")
    @Timed
    public ResponseEntity<Void> deleteGenre(@PathVariable Long id) {
        log.debug("REST request to delete Genre : {}", id);
        genreService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
