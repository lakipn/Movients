package com.lazarmilosavljevic.movients.web.rest;

import com.lazarmilosavljevic.movients.MovientsApp;

import com.lazarmilosavljevic.movients.domain.Torrent;
import com.lazarmilosavljevic.movients.domain.Movie;
import com.lazarmilosavljevic.movients.repository.TorrentRepository;
import com.lazarmilosavljevic.movients.service.TorrentService;
import com.lazarmilosavljevic.movients.service.dto.TorrentDTO;
import com.lazarmilosavljevic.movients.service.mapper.TorrentMapper;
import com.lazarmilosavljevic.movients.web.rest.errors.ExceptionTranslator;
import com.lazarmilosavljevic.movients.service.dto.TorrentCriteria;
import com.lazarmilosavljevic.movients.service.TorrentQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;


import static com.lazarmilosavljevic.movients.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TorrentResource REST controller.
 *
 * @see TorrentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MovientsApp.class)
public class TorrentResourceIntTest {

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String DEFAULT_HASH = "AAAAAAAAAA";
    private static final String UPDATED_HASH = "BBBBBBBBBB";

    private static final String DEFAULT_QUALITY = "AAAAAAAAAA";
    private static final String UPDATED_QUALITY = "BBBBBBBBBB";

    private static final String DEFAULT_SIZE = "AAAAAAAAAA";
    private static final String UPDATED_SIZE = "BBBBBBBBBB";

    @Autowired
    private TorrentRepository torrentRepository;

    @Autowired
    private TorrentMapper torrentMapper;

    @Autowired
    private TorrentService torrentService;

    @Autowired
    private TorrentQueryService torrentQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTorrentMockMvc;

    private Torrent torrent;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TorrentResource torrentResource = new TorrentResource(torrentService, torrentQueryService);
        this.restTorrentMockMvc = MockMvcBuilders.standaloneSetup(torrentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Torrent createEntity(EntityManager em) {
        Torrent torrent = new Torrent()
            .url(DEFAULT_URL)
            .hash(DEFAULT_HASH)
            .quality(DEFAULT_QUALITY)
            .size(DEFAULT_SIZE);
        return torrent;
    }

    @Before
    public void initTest() {
        torrent = createEntity(em);
    }

    @Test
    @Transactional
    public void createTorrent() throws Exception {
        int databaseSizeBeforeCreate = torrentRepository.findAll().size();

        // Create the Torrent
        TorrentDTO torrentDTO = torrentMapper.toDto(torrent);
        restTorrentMockMvc.perform(post("/api/torrents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(torrentDTO)))
            .andExpect(status().isCreated());

        // Validate the Torrent in the database
        List<Torrent> torrentList = torrentRepository.findAll();
        assertThat(torrentList).hasSize(databaseSizeBeforeCreate + 1);
        Torrent testTorrent = torrentList.get(torrentList.size() - 1);
        assertThat(testTorrent.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testTorrent.getHash()).isEqualTo(DEFAULT_HASH);
        assertThat(testTorrent.getQuality()).isEqualTo(DEFAULT_QUALITY);
        assertThat(testTorrent.getSize()).isEqualTo(DEFAULT_SIZE);
    }

    @Test
    @Transactional
    public void createTorrentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = torrentRepository.findAll().size();

        // Create the Torrent with an existing ID
        torrent.setId(1L);
        TorrentDTO torrentDTO = torrentMapper.toDto(torrent);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTorrentMockMvc.perform(post("/api/torrents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(torrentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Torrent in the database
        List<Torrent> torrentList = torrentRepository.findAll();
        assertThat(torrentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTorrents() throws Exception {
        // Initialize the database
        torrentRepository.saveAndFlush(torrent);

        // Get all the torrentList
        restTorrentMockMvc.perform(get("/api/torrents?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(torrent.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].hash").value(hasItem(DEFAULT_HASH.toString())))
            .andExpect(jsonPath("$.[*].quality").value(hasItem(DEFAULT_QUALITY.toString())))
            .andExpect(jsonPath("$.[*].size").value(hasItem(DEFAULT_SIZE.toString())));
    }
    
    @Test
    @Transactional
    public void getTorrent() throws Exception {
        // Initialize the database
        torrentRepository.saveAndFlush(torrent);

        // Get the torrent
        restTorrentMockMvc.perform(get("/api/torrents/{id}", torrent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(torrent.getId().intValue()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()))
            .andExpect(jsonPath("$.hash").value(DEFAULT_HASH.toString()))
            .andExpect(jsonPath("$.quality").value(DEFAULT_QUALITY.toString()))
            .andExpect(jsonPath("$.size").value(DEFAULT_SIZE.toString()));
    }

    @Test
    @Transactional
    public void getAllTorrentsByUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        torrentRepository.saveAndFlush(torrent);

        // Get all the torrentList where url equals to DEFAULT_URL
        defaultTorrentShouldBeFound("url.equals=" + DEFAULT_URL);

        // Get all the torrentList where url equals to UPDATED_URL
        defaultTorrentShouldNotBeFound("url.equals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllTorrentsByUrlIsInShouldWork() throws Exception {
        // Initialize the database
        torrentRepository.saveAndFlush(torrent);

        // Get all the torrentList where url in DEFAULT_URL or UPDATED_URL
        defaultTorrentShouldBeFound("url.in=" + DEFAULT_URL + "," + UPDATED_URL);

        // Get all the torrentList where url equals to UPDATED_URL
        defaultTorrentShouldNotBeFound("url.in=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllTorrentsByUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        torrentRepository.saveAndFlush(torrent);

        // Get all the torrentList where url is not null
        defaultTorrentShouldBeFound("url.specified=true");

        // Get all the torrentList where url is null
        defaultTorrentShouldNotBeFound("url.specified=false");
    }

    @Test
    @Transactional
    public void getAllTorrentsByHashIsEqualToSomething() throws Exception {
        // Initialize the database
        torrentRepository.saveAndFlush(torrent);

        // Get all the torrentList where hash equals to DEFAULT_HASH
        defaultTorrentShouldBeFound("hash.equals=" + DEFAULT_HASH);

        // Get all the torrentList where hash equals to UPDATED_HASH
        defaultTorrentShouldNotBeFound("hash.equals=" + UPDATED_HASH);
    }

    @Test
    @Transactional
    public void getAllTorrentsByHashIsInShouldWork() throws Exception {
        // Initialize the database
        torrentRepository.saveAndFlush(torrent);

        // Get all the torrentList where hash in DEFAULT_HASH or UPDATED_HASH
        defaultTorrentShouldBeFound("hash.in=" + DEFAULT_HASH + "," + UPDATED_HASH);

        // Get all the torrentList where hash equals to UPDATED_HASH
        defaultTorrentShouldNotBeFound("hash.in=" + UPDATED_HASH);
    }

    @Test
    @Transactional
    public void getAllTorrentsByHashIsNullOrNotNull() throws Exception {
        // Initialize the database
        torrentRepository.saveAndFlush(torrent);

        // Get all the torrentList where hash is not null
        defaultTorrentShouldBeFound("hash.specified=true");

        // Get all the torrentList where hash is null
        defaultTorrentShouldNotBeFound("hash.specified=false");
    }

    @Test
    @Transactional
    public void getAllTorrentsByQualityIsEqualToSomething() throws Exception {
        // Initialize the database
        torrentRepository.saveAndFlush(torrent);

        // Get all the torrentList where quality equals to DEFAULT_QUALITY
        defaultTorrentShouldBeFound("quality.equals=" + DEFAULT_QUALITY);

        // Get all the torrentList where quality equals to UPDATED_QUALITY
        defaultTorrentShouldNotBeFound("quality.equals=" + UPDATED_QUALITY);
    }

    @Test
    @Transactional
    public void getAllTorrentsByQualityIsInShouldWork() throws Exception {
        // Initialize the database
        torrentRepository.saveAndFlush(torrent);

        // Get all the torrentList where quality in DEFAULT_QUALITY or UPDATED_QUALITY
        defaultTorrentShouldBeFound("quality.in=" + DEFAULT_QUALITY + "," + UPDATED_QUALITY);

        // Get all the torrentList where quality equals to UPDATED_QUALITY
        defaultTorrentShouldNotBeFound("quality.in=" + UPDATED_QUALITY);
    }

    @Test
    @Transactional
    public void getAllTorrentsByQualityIsNullOrNotNull() throws Exception {
        // Initialize the database
        torrentRepository.saveAndFlush(torrent);

        // Get all the torrentList where quality is not null
        defaultTorrentShouldBeFound("quality.specified=true");

        // Get all the torrentList where quality is null
        defaultTorrentShouldNotBeFound("quality.specified=false");
    }

    @Test
    @Transactional
    public void getAllTorrentsBySizeIsEqualToSomething() throws Exception {
        // Initialize the database
        torrentRepository.saveAndFlush(torrent);

        // Get all the torrentList where size equals to DEFAULT_SIZE
        defaultTorrentShouldBeFound("size.equals=" + DEFAULT_SIZE);

        // Get all the torrentList where size equals to UPDATED_SIZE
        defaultTorrentShouldNotBeFound("size.equals=" + UPDATED_SIZE);
    }

    @Test
    @Transactional
    public void getAllTorrentsBySizeIsInShouldWork() throws Exception {
        // Initialize the database
        torrentRepository.saveAndFlush(torrent);

        // Get all the torrentList where size in DEFAULT_SIZE or UPDATED_SIZE
        defaultTorrentShouldBeFound("size.in=" + DEFAULT_SIZE + "," + UPDATED_SIZE);

        // Get all the torrentList where size equals to UPDATED_SIZE
        defaultTorrentShouldNotBeFound("size.in=" + UPDATED_SIZE);
    }

    @Test
    @Transactional
    public void getAllTorrentsBySizeIsNullOrNotNull() throws Exception {
        // Initialize the database
        torrentRepository.saveAndFlush(torrent);

        // Get all the torrentList where size is not null
        defaultTorrentShouldBeFound("size.specified=true");

        // Get all the torrentList where size is null
        defaultTorrentShouldNotBeFound("size.specified=false");
    }

    @Test
    @Transactional
    public void getAllTorrentsByMovieIsEqualToSomething() throws Exception {
        // Initialize the database
        Movie movie = MovieResourceIntTest.createEntity(em);
        em.persist(movie);
        em.flush();
        torrent.setMovie(movie);
        torrentRepository.saveAndFlush(torrent);
        Long movieId = movie.getId();

        // Get all the torrentList where movie equals to movieId
        defaultTorrentShouldBeFound("movieId.equals=" + movieId);

        // Get all the torrentList where movie equals to movieId + 1
        defaultTorrentShouldNotBeFound("movieId.equals=" + (movieId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultTorrentShouldBeFound(String filter) throws Exception {
        restTorrentMockMvc.perform(get("/api/torrents?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(torrent.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].hash").value(hasItem(DEFAULT_HASH.toString())))
            .andExpect(jsonPath("$.[*].quality").value(hasItem(DEFAULT_QUALITY.toString())))
            .andExpect(jsonPath("$.[*].size").value(hasItem(DEFAULT_SIZE.toString())));

        // Check, that the count call also returns 1
        restTorrentMockMvc.perform(get("/api/torrents/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultTorrentShouldNotBeFound(String filter) throws Exception {
        restTorrentMockMvc.perform(get("/api/torrents?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTorrentMockMvc.perform(get("/api/torrents/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingTorrent() throws Exception {
        // Get the torrent
        restTorrentMockMvc.perform(get("/api/torrents/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTorrent() throws Exception {
        // Initialize the database
        torrentRepository.saveAndFlush(torrent);

        int databaseSizeBeforeUpdate = torrentRepository.findAll().size();

        // Update the torrent
        Torrent updatedTorrent = torrentRepository.findById(torrent.getId()).get();
        // Disconnect from session so that the updates on updatedTorrent are not directly saved in db
        em.detach(updatedTorrent);
        updatedTorrent
            .url(UPDATED_URL)
            .hash(UPDATED_HASH)
            .quality(UPDATED_QUALITY)
            .size(UPDATED_SIZE);
        TorrentDTO torrentDTO = torrentMapper.toDto(updatedTorrent);

        restTorrentMockMvc.perform(put("/api/torrents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(torrentDTO)))
            .andExpect(status().isOk());

        // Validate the Torrent in the database
        List<Torrent> torrentList = torrentRepository.findAll();
        assertThat(torrentList).hasSize(databaseSizeBeforeUpdate);
        Torrent testTorrent = torrentList.get(torrentList.size() - 1);
        assertThat(testTorrent.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testTorrent.getHash()).isEqualTo(UPDATED_HASH);
        assertThat(testTorrent.getQuality()).isEqualTo(UPDATED_QUALITY);
        assertThat(testTorrent.getSize()).isEqualTo(UPDATED_SIZE);
    }

    @Test
    @Transactional
    public void updateNonExistingTorrent() throws Exception {
        int databaseSizeBeforeUpdate = torrentRepository.findAll().size();

        // Create the Torrent
        TorrentDTO torrentDTO = torrentMapper.toDto(torrent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTorrentMockMvc.perform(put("/api/torrents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(torrentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Torrent in the database
        List<Torrent> torrentList = torrentRepository.findAll();
        assertThat(torrentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTorrent() throws Exception {
        // Initialize the database
        torrentRepository.saveAndFlush(torrent);

        int databaseSizeBeforeDelete = torrentRepository.findAll().size();

        // Get the torrent
        restTorrentMockMvc.perform(delete("/api/torrents/{id}", torrent.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Torrent> torrentList = torrentRepository.findAll();
        assertThat(torrentList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Torrent.class);
        Torrent torrent1 = new Torrent();
        torrent1.setId(1L);
        Torrent torrent2 = new Torrent();
        torrent2.setId(torrent1.getId());
        assertThat(torrent1).isEqualTo(torrent2);
        torrent2.setId(2L);
        assertThat(torrent1).isNotEqualTo(torrent2);
        torrent1.setId(null);
        assertThat(torrent1).isNotEqualTo(torrent2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TorrentDTO.class);
        TorrentDTO torrentDTO1 = new TorrentDTO();
        torrentDTO1.setId(1L);
        TorrentDTO torrentDTO2 = new TorrentDTO();
        assertThat(torrentDTO1).isNotEqualTo(torrentDTO2);
        torrentDTO2.setId(torrentDTO1.getId());
        assertThat(torrentDTO1).isEqualTo(torrentDTO2);
        torrentDTO2.setId(2L);
        assertThat(torrentDTO1).isNotEqualTo(torrentDTO2);
        torrentDTO1.setId(null);
        assertThat(torrentDTO1).isNotEqualTo(torrentDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(torrentMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(torrentMapper.fromId(null)).isNull();
    }
}
