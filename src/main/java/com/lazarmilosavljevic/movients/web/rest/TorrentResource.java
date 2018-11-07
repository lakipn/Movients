package com.lazarmilosavljevic.movients.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lazarmilosavljevic.movients.service.TorrentService;
import com.lazarmilosavljevic.movients.web.rest.errors.BadRequestAlertException;
import com.lazarmilosavljevic.movients.web.rest.util.HeaderUtil;
import com.lazarmilosavljevic.movients.web.rest.util.PaginationUtil;
import com.lazarmilosavljevic.movients.service.dto.TorrentDTO;
import com.lazarmilosavljevic.movients.service.dto.TorrentCriteria;
import com.lazarmilosavljevic.movients.service.TorrentQueryService;
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
 * REST controller for managing Torrent.
 */
@RestController
@RequestMapping("/api")
public class TorrentResource {

    private final Logger log = LoggerFactory.getLogger(TorrentResource.class);

    private static final String ENTITY_NAME = "torrent";

    private final TorrentService torrentService;

    private final TorrentQueryService torrentQueryService;

    public TorrentResource(TorrentService torrentService, TorrentQueryService torrentQueryService) {
        this.torrentService = torrentService;
        this.torrentQueryService = torrentQueryService;
    }

    /**
     * POST  /torrents : Create a new torrent.
     *
     * @param torrentDTO the torrentDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new torrentDTO, or with status 400 (Bad Request) if the torrent has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/torrents")
    @Timed
    public ResponseEntity<TorrentDTO> createTorrent(@Valid @RequestBody TorrentDTO torrentDTO) throws URISyntaxException {
        log.debug("REST request to save Torrent : {}", torrentDTO);
        if (torrentDTO.getId() != null) {
            throw new BadRequestAlertException("A new torrent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TorrentDTO result = torrentService.save(torrentDTO);
        return ResponseEntity.created(new URI("/api/torrents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /torrents : Updates an existing torrent.
     *
     * @param torrentDTO the torrentDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated torrentDTO,
     * or with status 400 (Bad Request) if the torrentDTO is not valid,
     * or with status 500 (Internal Server Error) if the torrentDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/torrents")
    @Timed
    public ResponseEntity<TorrentDTO> updateTorrent(@Valid @RequestBody TorrentDTO torrentDTO) throws URISyntaxException {
        log.debug("REST request to update Torrent : {}", torrentDTO);
        if (torrentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TorrentDTO result = torrentService.save(torrentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, torrentDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /torrents : get all the torrents.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of torrents in body
     */
    @GetMapping("/torrents")
    @Timed
    public ResponseEntity<List<TorrentDTO>> getAllTorrents(TorrentCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Torrents by criteria: {}", criteria);
        Page<TorrentDTO> page = torrentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/torrents");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /torrents/count : count all the torrents.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/torrents/count")
    @Timed
    public ResponseEntity<Long> countTorrents(TorrentCriteria criteria) {
        log.debug("REST request to count Torrents by criteria: {}", criteria);
        return ResponseEntity.ok().body(torrentQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /torrents/:id : get the "id" torrent.
     *
     * @param id the id of the torrentDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the torrentDTO, or with status 404 (Not Found)
     */
    @GetMapping("/torrents/{id}")
    @Timed
    public ResponseEntity<TorrentDTO> getTorrent(@PathVariable Long id) {
        log.debug("REST request to get Torrent : {}", id);
        Optional<TorrentDTO> torrentDTO = torrentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(torrentDTO);
    }

    /**
     * DELETE  /torrents/:id : delete the "id" torrent.
     *
     * @param id the id of the torrentDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/torrents/{id}")
    @Timed
    public ResponseEntity<Void> deleteTorrent(@PathVariable Long id) {
        log.debug("REST request to delete Torrent : {}", id);
        torrentService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
