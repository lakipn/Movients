package com.lazarmilosavljevic.movients.web.rest;

import com.lazarmilosavljevic.movients.MovientsApp;

import com.lazarmilosavljevic.movients.domain.Cast;
import com.lazarmilosavljevic.movients.domain.Movie;
import com.lazarmilosavljevic.movients.repository.CastRepository;
import com.lazarmilosavljevic.movients.service.CastService;
import com.lazarmilosavljevic.movients.service.dto.CastDTO;
import com.lazarmilosavljevic.movients.service.mapper.CastMapper;
import com.lazarmilosavljevic.movients.web.rest.errors.ExceptionTranslator;
import com.lazarmilosavljevic.movients.service.dto.CastCriteria;
import com.lazarmilosavljevic.movients.service.CastQueryService;

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
 * Test class for the CastResource REST controller.
 *
 * @see CastResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MovientsApp.class)
public class CastResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CHARACTER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CHARACTER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE = "BBBBBBBBBB";

    private static final String DEFAULT_IMDB = "AAAAAAAAAA";
    private static final String UPDATED_IMDB = "BBBBBBBBBB";

    @Autowired
    private CastRepository castRepository;

    @Autowired
    private CastMapper castMapper;

    @Autowired
    private CastService castService;

    @Autowired
    private CastQueryService castQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCastMockMvc;

    private Cast cast;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CastResource castResource = new CastResource(castService, castQueryService);
        this.restCastMockMvc = MockMvcBuilders.standaloneSetup(castResource)
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
    public static Cast createEntity(EntityManager em) {
        Cast cast = new Cast()
            .name(DEFAULT_NAME)
            .characterName(DEFAULT_CHARACTER_NAME)
            .image(DEFAULT_IMAGE)
            .imdb(DEFAULT_IMDB);
        return cast;
    }

    @Before
    public void initTest() {
        cast = createEntity(em);
    }

    @Test
    @Transactional
    public void createCast() throws Exception {
        int databaseSizeBeforeCreate = castRepository.findAll().size();

        // Create the Cast
        CastDTO castDTO = castMapper.toDto(cast);
        restCastMockMvc.perform(post("/api/casts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(castDTO)))
            .andExpect(status().isCreated());

        // Validate the Cast in the database
        List<Cast> castList = castRepository.findAll();
        assertThat(castList).hasSize(databaseSizeBeforeCreate + 1);
        Cast testCast = castList.get(castList.size() - 1);
        assertThat(testCast.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCast.getCharacterName()).isEqualTo(DEFAULT_CHARACTER_NAME);
        assertThat(testCast.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testCast.getImdb()).isEqualTo(DEFAULT_IMDB);
    }

    @Test
    @Transactional
    public void createCastWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = castRepository.findAll().size();

        // Create the Cast with an existing ID
        cast.setId(1L);
        CastDTO castDTO = castMapper.toDto(cast);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCastMockMvc.perform(post("/api/casts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(castDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Cast in the database
        List<Cast> castList = castRepository.findAll();
        assertThat(castList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCasts() throws Exception {
        // Initialize the database
        castRepository.saveAndFlush(cast);

        // Get all the castList
        restCastMockMvc.perform(get("/api/casts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cast.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].characterName").value(hasItem(DEFAULT_CHARACTER_NAME.toString())))
            .andExpect(jsonPath("$.[*].image").value(hasItem(DEFAULT_IMAGE.toString())))
            .andExpect(jsonPath("$.[*].imdb").value(hasItem(DEFAULT_IMDB.toString())));
    }
    
    @Test
    @Transactional
    public void getCast() throws Exception {
        // Initialize the database
        castRepository.saveAndFlush(cast);

        // Get the cast
        restCastMockMvc.perform(get("/api/casts/{id}", cast.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cast.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.characterName").value(DEFAULT_CHARACTER_NAME.toString()))
            .andExpect(jsonPath("$.image").value(DEFAULT_IMAGE.toString()))
            .andExpect(jsonPath("$.imdb").value(DEFAULT_IMDB.toString()));
    }

    @Test
    @Transactional
    public void getAllCastsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        castRepository.saveAndFlush(cast);

        // Get all the castList where name equals to DEFAULT_NAME
        defaultCastShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the castList where name equals to UPDATED_NAME
        defaultCastShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCastsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        castRepository.saveAndFlush(cast);

        // Get all the castList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCastShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the castList where name equals to UPDATED_NAME
        defaultCastShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCastsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        castRepository.saveAndFlush(cast);

        // Get all the castList where name is not null
        defaultCastShouldBeFound("name.specified=true");

        // Get all the castList where name is null
        defaultCastShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllCastsByCharacterNameIsEqualToSomething() throws Exception {
        // Initialize the database
        castRepository.saveAndFlush(cast);

        // Get all the castList where characterName equals to DEFAULT_CHARACTER_NAME
        defaultCastShouldBeFound("characterName.equals=" + DEFAULT_CHARACTER_NAME);

        // Get all the castList where characterName equals to UPDATED_CHARACTER_NAME
        defaultCastShouldNotBeFound("characterName.equals=" + UPDATED_CHARACTER_NAME);
    }

    @Test
    @Transactional
    public void getAllCastsByCharacterNameIsInShouldWork() throws Exception {
        // Initialize the database
        castRepository.saveAndFlush(cast);

        // Get all the castList where characterName in DEFAULT_CHARACTER_NAME or UPDATED_CHARACTER_NAME
        defaultCastShouldBeFound("characterName.in=" + DEFAULT_CHARACTER_NAME + "," + UPDATED_CHARACTER_NAME);

        // Get all the castList where characterName equals to UPDATED_CHARACTER_NAME
        defaultCastShouldNotBeFound("characterName.in=" + UPDATED_CHARACTER_NAME);
    }

    @Test
    @Transactional
    public void getAllCastsByCharacterNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        castRepository.saveAndFlush(cast);

        // Get all the castList where characterName is not null
        defaultCastShouldBeFound("characterName.specified=true");

        // Get all the castList where characterName is null
        defaultCastShouldNotBeFound("characterName.specified=false");
    }

    @Test
    @Transactional
    public void getAllCastsByImageIsEqualToSomething() throws Exception {
        // Initialize the database
        castRepository.saveAndFlush(cast);

        // Get all the castList where image equals to DEFAULT_IMAGE
        defaultCastShouldBeFound("image.equals=" + DEFAULT_IMAGE);

        // Get all the castList where image equals to UPDATED_IMAGE
        defaultCastShouldNotBeFound("image.equals=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    public void getAllCastsByImageIsInShouldWork() throws Exception {
        // Initialize the database
        castRepository.saveAndFlush(cast);

        // Get all the castList where image in DEFAULT_IMAGE or UPDATED_IMAGE
        defaultCastShouldBeFound("image.in=" + DEFAULT_IMAGE + "," + UPDATED_IMAGE);

        // Get all the castList where image equals to UPDATED_IMAGE
        defaultCastShouldNotBeFound("image.in=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    public void getAllCastsByImageIsNullOrNotNull() throws Exception {
        // Initialize the database
        castRepository.saveAndFlush(cast);

        // Get all the castList where image is not null
        defaultCastShouldBeFound("image.specified=true");

        // Get all the castList where image is null
        defaultCastShouldNotBeFound("image.specified=false");
    }

    @Test
    @Transactional
    public void getAllCastsByImdbIsEqualToSomething() throws Exception {
        // Initialize the database
        castRepository.saveAndFlush(cast);

        // Get all the castList where imdb equals to DEFAULT_IMDB
        defaultCastShouldBeFound("imdb.equals=" + DEFAULT_IMDB);

        // Get all the castList where imdb equals to UPDATED_IMDB
        defaultCastShouldNotBeFound("imdb.equals=" + UPDATED_IMDB);
    }

    @Test
    @Transactional
    public void getAllCastsByImdbIsInShouldWork() throws Exception {
        // Initialize the database
        castRepository.saveAndFlush(cast);

        // Get all the castList where imdb in DEFAULT_IMDB or UPDATED_IMDB
        defaultCastShouldBeFound("imdb.in=" + DEFAULT_IMDB + "," + UPDATED_IMDB);

        // Get all the castList where imdb equals to UPDATED_IMDB
        defaultCastShouldNotBeFound("imdb.in=" + UPDATED_IMDB);
    }

    @Test
    @Transactional
    public void getAllCastsByImdbIsNullOrNotNull() throws Exception {
        // Initialize the database
        castRepository.saveAndFlush(cast);

        // Get all the castList where imdb is not null
        defaultCastShouldBeFound("imdb.specified=true");

        // Get all the castList where imdb is null
        defaultCastShouldNotBeFound("imdb.specified=false");
    }

    @Test
    @Transactional
    public void getAllCastsByMovieIsEqualToSomething() throws Exception {
        // Initialize the database
        Movie movie = MovieResourceIntTest.createEntity(em);
        em.persist(movie);
        em.flush();
        cast.setMovie(movie);
        castRepository.saveAndFlush(cast);
        Long movieId = movie.getId();

        // Get all the castList where movie equals to movieId
        defaultCastShouldBeFound("movieId.equals=" + movieId);

        // Get all the castList where movie equals to movieId + 1
        defaultCastShouldNotBeFound("movieId.equals=" + (movieId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultCastShouldBeFound(String filter) throws Exception {
        restCastMockMvc.perform(get("/api/casts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cast.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].characterName").value(hasItem(DEFAULT_CHARACTER_NAME.toString())))
            .andExpect(jsonPath("$.[*].image").value(hasItem(DEFAULT_IMAGE.toString())))
            .andExpect(jsonPath("$.[*].imdb").value(hasItem(DEFAULT_IMDB.toString())));

        // Check, that the count call also returns 1
        restCastMockMvc.perform(get("/api/casts/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultCastShouldNotBeFound(String filter) throws Exception {
        restCastMockMvc.perform(get("/api/casts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCastMockMvc.perform(get("/api/casts/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingCast() throws Exception {
        // Get the cast
        restCastMockMvc.perform(get("/api/casts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCast() throws Exception {
        // Initialize the database
        castRepository.saveAndFlush(cast);

        int databaseSizeBeforeUpdate = castRepository.findAll().size();

        // Update the cast
        Cast updatedCast = castRepository.findById(cast.getId()).get();
        // Disconnect from session so that the updates on updatedCast are not directly saved in db
        em.detach(updatedCast);
        updatedCast
            .name(UPDATED_NAME)
            .characterName(UPDATED_CHARACTER_NAME)
            .image(UPDATED_IMAGE)
            .imdb(UPDATED_IMDB);
        CastDTO castDTO = castMapper.toDto(updatedCast);

        restCastMockMvc.perform(put("/api/casts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(castDTO)))
            .andExpect(status().isOk());

        // Validate the Cast in the database
        List<Cast> castList = castRepository.findAll();
        assertThat(castList).hasSize(databaseSizeBeforeUpdate);
        Cast testCast = castList.get(castList.size() - 1);
        assertThat(testCast.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCast.getCharacterName()).isEqualTo(UPDATED_CHARACTER_NAME);
        assertThat(testCast.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testCast.getImdb()).isEqualTo(UPDATED_IMDB);
    }

    @Test
    @Transactional
    public void updateNonExistingCast() throws Exception {
        int databaseSizeBeforeUpdate = castRepository.findAll().size();

        // Create the Cast
        CastDTO castDTO = castMapper.toDto(cast);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCastMockMvc.perform(put("/api/casts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(castDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Cast in the database
        List<Cast> castList = castRepository.findAll();
        assertThat(castList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCast() throws Exception {
        // Initialize the database
        castRepository.saveAndFlush(cast);

        int databaseSizeBeforeDelete = castRepository.findAll().size();

        // Get the cast
        restCastMockMvc.perform(delete("/api/casts/{id}", cast.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Cast> castList = castRepository.findAll();
        assertThat(castList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cast.class);
        Cast cast1 = new Cast();
        cast1.setId(1L);
        Cast cast2 = new Cast();
        cast2.setId(cast1.getId());
        assertThat(cast1).isEqualTo(cast2);
        cast2.setId(2L);
        assertThat(cast1).isNotEqualTo(cast2);
        cast1.setId(null);
        assertThat(cast1).isNotEqualTo(cast2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CastDTO.class);
        CastDTO castDTO1 = new CastDTO();
        castDTO1.setId(1L);
        CastDTO castDTO2 = new CastDTO();
        assertThat(castDTO1).isNotEqualTo(castDTO2);
        castDTO2.setId(castDTO1.getId());
        assertThat(castDTO1).isEqualTo(castDTO2);
        castDTO2.setId(2L);
        assertThat(castDTO1).isNotEqualTo(castDTO2);
        castDTO1.setId(null);
        assertThat(castDTO1).isNotEqualTo(castDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(castMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(castMapper.fromId(null)).isNull();
    }
}
