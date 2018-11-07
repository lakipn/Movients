package com.lazarmilosavljevic.movients.web.rest;

import com.lazarmilosavljevic.movients.MovientsApp;

import com.lazarmilosavljevic.movients.domain.Image;
import com.lazarmilosavljevic.movients.domain.Movie;
import com.lazarmilosavljevic.movients.repository.ImageRepository;
import com.lazarmilosavljevic.movients.service.ImageService;
import com.lazarmilosavljevic.movients.service.dto.ImageDTO;
import com.lazarmilosavljevic.movients.service.mapper.ImageMapper;
import com.lazarmilosavljevic.movients.web.rest.errors.ExceptionTranslator;
import com.lazarmilosavljevic.movients.service.dto.ImageCriteria;
import com.lazarmilosavljevic.movients.service.ImageQueryService;

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

import com.lazarmilosavljevic.movients.domain.enumeration.ImageType;
/**
 * Test class for the ImageResource REST controller.
 *
 * @see ImageResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MovientsApp.class)
public class ImageResourceIntTest {

    private static final String DEFAULT_MEDIUM = "AAAAAAAAAA";
    private static final String UPDATED_MEDIUM = "BBBBBBBBBB";

    private static final String DEFAULT_LARGE = "AAAAAAAAAA";
    private static final String UPDATED_LARGE = "BBBBBBBBBB";

    private static final ImageType DEFAULT_TYPE = ImageType.COVER;
    private static final ImageType UPDATED_TYPE = ImageType.SCREENSHOT;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ImageMapper imageMapper;

    @Autowired
    private ImageService imageService;

    @Autowired
    private ImageQueryService imageQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restImageMockMvc;

    private Image image;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ImageResource imageResource = new ImageResource(imageService, imageQueryService);
        this.restImageMockMvc = MockMvcBuilders.standaloneSetup(imageResource)
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
    public static Image createEntity(EntityManager em) {
        Image image = new Image()
            .medium(DEFAULT_MEDIUM)
            .large(DEFAULT_LARGE)
            .type(DEFAULT_TYPE);
        return image;
    }

    @Before
    public void initTest() {
        image = createEntity(em);
    }

    @Test
    @Transactional
    public void createImage() throws Exception {
        int databaseSizeBeforeCreate = imageRepository.findAll().size();

        // Create the Image
        ImageDTO imageDTO = imageMapper.toDto(image);
        restImageMockMvc.perform(post("/api/images")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(imageDTO)))
            .andExpect(status().isCreated());

        // Validate the Image in the database
        List<Image> imageList = imageRepository.findAll();
        assertThat(imageList).hasSize(databaseSizeBeforeCreate + 1);
        Image testImage = imageList.get(imageList.size() - 1);
        assertThat(testImage.getMedium()).isEqualTo(DEFAULT_MEDIUM);
        assertThat(testImage.getLarge()).isEqualTo(DEFAULT_LARGE);
        assertThat(testImage.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void createImageWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = imageRepository.findAll().size();

        // Create the Image with an existing ID
        image.setId(1L);
        ImageDTO imageDTO = imageMapper.toDto(image);

        // An entity with an existing ID cannot be created, so this API call must fail
        restImageMockMvc.perform(post("/api/images")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(imageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Image in the database
        List<Image> imageList = imageRepository.findAll();
        assertThat(imageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllImages() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList
        restImageMockMvc.perform(get("/api/images?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(image.getId().intValue())))
            .andExpect(jsonPath("$.[*].medium").value(hasItem(DEFAULT_MEDIUM.toString())))
            .andExpect(jsonPath("$.[*].large").value(hasItem(DEFAULT_LARGE.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }
    
    @Test
    @Transactional
    public void getImage() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get the image
        restImageMockMvc.perform(get("/api/images/{id}", image.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(image.getId().intValue()))
            .andExpect(jsonPath("$.medium").value(DEFAULT_MEDIUM.toString()))
            .andExpect(jsonPath("$.large").value(DEFAULT_LARGE.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getAllImagesByMediumIsEqualToSomething() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where medium equals to DEFAULT_MEDIUM
        defaultImageShouldBeFound("medium.equals=" + DEFAULT_MEDIUM);

        // Get all the imageList where medium equals to UPDATED_MEDIUM
        defaultImageShouldNotBeFound("medium.equals=" + UPDATED_MEDIUM);
    }

    @Test
    @Transactional
    public void getAllImagesByMediumIsInShouldWork() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where medium in DEFAULT_MEDIUM or UPDATED_MEDIUM
        defaultImageShouldBeFound("medium.in=" + DEFAULT_MEDIUM + "," + UPDATED_MEDIUM);

        // Get all the imageList where medium equals to UPDATED_MEDIUM
        defaultImageShouldNotBeFound("medium.in=" + UPDATED_MEDIUM);
    }

    @Test
    @Transactional
    public void getAllImagesByMediumIsNullOrNotNull() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where medium is not null
        defaultImageShouldBeFound("medium.specified=true");

        // Get all the imageList where medium is null
        defaultImageShouldNotBeFound("medium.specified=false");
    }

    @Test
    @Transactional
    public void getAllImagesByLargeIsEqualToSomething() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where large equals to DEFAULT_LARGE
        defaultImageShouldBeFound("large.equals=" + DEFAULT_LARGE);

        // Get all the imageList where large equals to UPDATED_LARGE
        defaultImageShouldNotBeFound("large.equals=" + UPDATED_LARGE);
    }

    @Test
    @Transactional
    public void getAllImagesByLargeIsInShouldWork() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where large in DEFAULT_LARGE or UPDATED_LARGE
        defaultImageShouldBeFound("large.in=" + DEFAULT_LARGE + "," + UPDATED_LARGE);

        // Get all the imageList where large equals to UPDATED_LARGE
        defaultImageShouldNotBeFound("large.in=" + UPDATED_LARGE);
    }

    @Test
    @Transactional
    public void getAllImagesByLargeIsNullOrNotNull() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where large is not null
        defaultImageShouldBeFound("large.specified=true");

        // Get all the imageList where large is null
        defaultImageShouldNotBeFound("large.specified=false");
    }

    @Test
    @Transactional
    public void getAllImagesByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where type equals to DEFAULT_TYPE
        defaultImageShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the imageList where type equals to UPDATED_TYPE
        defaultImageShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllImagesByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultImageShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the imageList where type equals to UPDATED_TYPE
        defaultImageShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllImagesByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where type is not null
        defaultImageShouldBeFound("type.specified=true");

        // Get all the imageList where type is null
        defaultImageShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllImagesByMovieIsEqualToSomething() throws Exception {
        // Initialize the database
        Movie movie = MovieResourceIntTest.createEntity(em);
        em.persist(movie);
        em.flush();
        image.setMovie(movie);
        imageRepository.saveAndFlush(image);
        Long movieId = movie.getId();

        // Get all the imageList where movie equals to movieId
        defaultImageShouldBeFound("movieId.equals=" + movieId);

        // Get all the imageList where movie equals to movieId + 1
        defaultImageShouldNotBeFound("movieId.equals=" + (movieId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultImageShouldBeFound(String filter) throws Exception {
        restImageMockMvc.perform(get("/api/images?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(image.getId().intValue())))
            .andExpect(jsonPath("$.[*].medium").value(hasItem(DEFAULT_MEDIUM.toString())))
            .andExpect(jsonPath("$.[*].large").value(hasItem(DEFAULT_LARGE.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));

        // Check, that the count call also returns 1
        restImageMockMvc.perform(get("/api/images/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultImageShouldNotBeFound(String filter) throws Exception {
        restImageMockMvc.perform(get("/api/images?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restImageMockMvc.perform(get("/api/images/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingImage() throws Exception {
        // Get the image
        restImageMockMvc.perform(get("/api/images/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateImage() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        int databaseSizeBeforeUpdate = imageRepository.findAll().size();

        // Update the image
        Image updatedImage = imageRepository.findById(image.getId()).get();
        // Disconnect from session so that the updates on updatedImage are not directly saved in db
        em.detach(updatedImage);
        updatedImage
            .medium(UPDATED_MEDIUM)
            .large(UPDATED_LARGE)
            .type(UPDATED_TYPE);
        ImageDTO imageDTO = imageMapper.toDto(updatedImage);

        restImageMockMvc.perform(put("/api/images")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(imageDTO)))
            .andExpect(status().isOk());

        // Validate the Image in the database
        List<Image> imageList = imageRepository.findAll();
        assertThat(imageList).hasSize(databaseSizeBeforeUpdate);
        Image testImage = imageList.get(imageList.size() - 1);
        assertThat(testImage.getMedium()).isEqualTo(UPDATED_MEDIUM);
        assertThat(testImage.getLarge()).isEqualTo(UPDATED_LARGE);
        assertThat(testImage.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingImage() throws Exception {
        int databaseSizeBeforeUpdate = imageRepository.findAll().size();

        // Create the Image
        ImageDTO imageDTO = imageMapper.toDto(image);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImageMockMvc.perform(put("/api/images")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(imageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Image in the database
        List<Image> imageList = imageRepository.findAll();
        assertThat(imageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteImage() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        int databaseSizeBeforeDelete = imageRepository.findAll().size();

        // Get the image
        restImageMockMvc.perform(delete("/api/images/{id}", image.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Image> imageList = imageRepository.findAll();
        assertThat(imageList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Image.class);
        Image image1 = new Image();
        image1.setId(1L);
        Image image2 = new Image();
        image2.setId(image1.getId());
        assertThat(image1).isEqualTo(image2);
        image2.setId(2L);
        assertThat(image1).isNotEqualTo(image2);
        image1.setId(null);
        assertThat(image1).isNotEqualTo(image2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ImageDTO.class);
        ImageDTO imageDTO1 = new ImageDTO();
        imageDTO1.setId(1L);
        ImageDTO imageDTO2 = new ImageDTO();
        assertThat(imageDTO1).isNotEqualTo(imageDTO2);
        imageDTO2.setId(imageDTO1.getId());
        assertThat(imageDTO1).isEqualTo(imageDTO2);
        imageDTO2.setId(2L);
        assertThat(imageDTO1).isNotEqualTo(imageDTO2);
        imageDTO1.setId(null);
        assertThat(imageDTO1).isNotEqualTo(imageDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(imageMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(imageMapper.fromId(null)).isNull();
    }
}
