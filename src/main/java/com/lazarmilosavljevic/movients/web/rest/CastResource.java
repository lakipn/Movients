package com.lazarmilosavljevic.movients.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lazarmilosavljevic.movients.service.CastService;
import com.lazarmilosavljevic.movients.web.rest.errors.BadRequestAlertException;
import com.lazarmilosavljevic.movients.web.rest.util.HeaderUtil;
import com.lazarmilosavljevic.movients.web.rest.util.PaginationUtil;
import com.lazarmilosavljevic.movients.service.dto.CastDTO;
import com.lazarmilosavljevic.movients.service.dto.CastCriteria;
import com.lazarmilosavljevic.movients.service.CastQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Cast.
 */
@RestController
@RequestMapping("/api")
public class CastResource {

    private final Logger log = LoggerFactory.getLogger(CastResource.class);

    private static final String ENTITY_NAME = "cast";

    private final CastService castService;

    private final CastQueryService castQueryService;

    public CastResource(CastService castService, CastQueryService castQueryService) {
        this.castService = castService;
        this.castQueryService = castQueryService;
    }

    /**
     * POST  /casts : Create a new cast.
     *
     * @param castDTO the castDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new castDTO, or with status 400 (Bad Request) if the cast has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/casts")
    @Timed
    public ResponseEntity<CastDTO> createCast(@Valid @RequestBody CastDTO castDTO) throws URISyntaxException {
        log.debug("REST request to save Cast : {}", castDTO);
        if (castDTO.getId() != null) {
            throw new BadRequestAlertException("A new cast cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CastDTO result = castService.save(castDTO);
        return ResponseEntity.created(new URI("/api/casts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /casts : Updates an existing cast.
     *
     * @param castDTO the castDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated castDTO,
     * or with status 400 (Bad Request) if the castDTO is not valid,
     * or with status 500 (Internal Server Error) if the castDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/casts")
    @Timed
    public ResponseEntity<CastDTO> updateCast(@Valid @RequestBody CastDTO castDTO) throws URISyntaxException {
        log.debug("REST request to update Cast : {}", castDTO);
        if (castDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CastDTO result = castService.save(castDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, castDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /casts : get all the casts.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of casts in body
     */
    @GetMapping("/casts")
    @Timed
    public ResponseEntity<List<CastDTO>> getAllCasts(CastCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Casts by criteria: {}", criteria);
        Page<CastDTO> page = castQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/casts");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /casts/count : count all the casts.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/casts/count")
    @Timed
    public ResponseEntity<Long> countCasts(CastCriteria criteria) {
        log.debug("REST request to count Casts by criteria: {}", criteria);
        return ResponseEntity.ok().body(castQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /casts/:id : get the "id" cast.
     *
     * @param id the id of the castDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the castDTO, or with status 404 (Not Found)
     */
    @GetMapping("/casts/{id}")
    @Timed
    public ResponseEntity<CastDTO> getCast(@PathVariable Long id) {
        log.debug("REST request to get Cast : {}", id);
        Optional<CastDTO> castDTO = castService.findOne(id);
        return ResponseUtil.wrapOrNotFound(castDTO);
    }

    /**
     * DELETE  /casts/:id : delete the "id" cast.
     *
     * @param id the id of the castDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/casts/{id}")
    @Timed
    public ResponseEntity<Void> deleteCast(@PathVariable Long id) {
        log.debug("REST request to delete Cast : {}", id);
        castService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
