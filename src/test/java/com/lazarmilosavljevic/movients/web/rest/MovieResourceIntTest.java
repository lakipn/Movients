package com.lazarmilosavljevic.movients.web.rest;

import com.lazarmilosavljevic.movients.MovientsApp;

import com.lazarmilosavljevic.movients.domain.Movie;
import com.lazarmilosavljevic.movients.domain.Cast;
import com.lazarmilosavljevic.movients.domain.Image;
import com.lazarmilosavljevic.movients.domain.Torrent;
import com.lazarmilosavljevic.movients.domain.Genre;
import com.lazarmilosavljevic.movients.repository.MovieRepository;
import com.lazarmilosavljevic.movients.service.MovieService;
import com.lazarmilosavljevic.movients.service.dto.MovieDTO;
import com.lazarmilosavljevic.movients.service.mapper.MovieMapper;
import com.lazarmilosavljevic.movients.web.rest.errors.ExceptionTranslator;
import com.lazarmilosavljevic.movients.service.dto.MovieCriteria;
import com.lazarmilosavljevic.movients.service.MovieQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;


import static com.lazarmilosavljevic.movients.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the MovieResource REST controller.
 *
 * @see MovieResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MovientsApp.class)
public class MovieResourceIntTest {

    private static final String DEFAULT_IMDB_CODE = "AAAAAAAAAA";
    private static final String UPDATED_IMDB_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_SLUG = "AAAAAAAAAA";
    private static final String UPDATED_SLUG = "BBBBBBBBBB";

    private static final Integer DEFAULT_YEAR = 1;
    private static final Integer UPDATED_YEAR = 2;

    private static final Double DEFAULT_RATING = 1D;
    private static final Double UPDATED_RATING = 2D;

    private static final Integer DEFAULT_RUNTIME = 1;
    private static final Integer UPDATED_RUNTIME = 2;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_YOUTUBE = "AAAAAAAAAA";
    private static final String UPDATED_YOUTUBE = "BBBBBBBBBB";

    private static final String DEFAULT_LANGUAGE = "AAAAAAAAAA";
    private static final String UPDATED_LANGUAGE = "BBBBBBBBBB";

    @Autowired
    private MovieRepository movieRepository;

    @Mock
    private MovieRepository movieRepositoryMock;

    @Autowired
    private MovieMapper movieMapper;

    @Mock
    private MovieService movieServiceMock;

    @Autowired
    private MovieService movieService;

    @Autowired
    private MovieQueryService movieQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMovieMockMvc;

    private Movie movie;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MovieResource movieResource = new MovieResource(movieService, movieQueryService);
        this.restMovieMockMvc = MockMvcBuilders.standaloneSetup(movieResource)
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
    public static Movie createEntity(EntityManager em) {
        Movie movie = new Movie()
            .imdbCode(DEFAULT_IMDB_CODE)
            .title(DEFAULT_TITLE)
            .slug(DEFAULT_SLUG)
            .year(DEFAULT_YEAR)
            .rating(DEFAULT_RATING)
            .runtime(DEFAULT_RUNTIME)
            .description(DEFAULT_DESCRIPTION)
            .youtube(DEFAULT_YOUTUBE)
            .language(DEFAULT_LANGUAGE);
        // Add required entity
        Genre genre = GenreResourceIntTest.createEntity(em);
        em.persist(genre);
        em.flush();
        movie.getGenres().add(genre);
        return movie;
    }

    @Before
    public void initTest() {
        movie = createEntity(em);
    }

    @Test
    @Transactional
    public void createMovie() throws Exception {
        int databaseSizeBeforeCreate = movieRepository.findAll().size();

        // Create the Movie
        MovieDTO movieDTO = movieMapper.toDto(movie);
        restMovieMockMvc.perform(post("/api/movies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(movieDTO)))
            .andExpect(status().isCreated());

        // Validate the Movie in the database
        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeCreate + 1);
        Movie testMovie = movieList.get(movieList.size() - 1);
        assertThat(testMovie.getImdbCode()).isEqualTo(DEFAULT_IMDB_CODE);
        assertThat(testMovie.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testMovie.getSlug()).isEqualTo(DEFAULT_SLUG);
        assertThat(testMovie.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testMovie.getRating()).isEqualTo(DEFAULT_RATING);
        assertThat(testMovie.getRuntime()).isEqualTo(DEFAULT_RUNTIME);
        assertThat(testMovie.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMovie.getYoutube()).isEqualTo(DEFAULT_YOUTUBE);
        assertThat(testMovie.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);
    }

    @Test
    @Transactional
    public void createMovieWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = movieRepository.findAll().size();

        // Create the Movie with an existing ID
        movie.setId(1L);
        MovieDTO movieDTO = movieMapper.toDto(movie);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMovieMockMvc.perform(post("/api/movies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(movieDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Movie in the database
        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllMovies() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList
        restMovieMockMvc.perform(get("/api/movies?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(movie.getId().intValue())))
            .andExpect(jsonPath("$.[*].imdbCode").value(hasItem(DEFAULT_IMDB_CODE.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].slug").value(hasItem(DEFAULT_SLUG.toString())))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING.doubleValue())))
            .andExpect(jsonPath("$.[*].runtime").value(hasItem(DEFAULT_RUNTIME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].youtube").value(hasItem(DEFAULT_YOUTUBE.toString())))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllMoviesWithEagerRelationshipsIsEnabled() throws Exception {
        MovieResource movieResource = new MovieResource(movieServiceMock, movieQueryService);
        when(movieServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restMovieMockMvc = MockMvcBuilders.standaloneSetup(movieResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restMovieMockMvc.perform(get("/api/movies?eagerload=true"))
        .andExpect(status().isOk());

        verify(movieServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllMoviesWithEagerRelationshipsIsNotEnabled() throws Exception {
        MovieResource movieResource = new MovieResource(movieServiceMock, movieQueryService);
            when(movieServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restMovieMockMvc = MockMvcBuilders.standaloneSetup(movieResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restMovieMockMvc.perform(get("/api/movies?eagerload=true"))
        .andExpect(status().isOk());

            verify(movieServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getMovie() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get the movie
        restMovieMockMvc.perform(get("/api/movies/{id}", movie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(movie.getId().intValue()))
            .andExpect(jsonPath("$.imdbCode").value(DEFAULT_IMDB_CODE.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.slug").value(DEFAULT_SLUG.toString()))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING.doubleValue()))
            .andExpect(jsonPath("$.runtime").value(DEFAULT_RUNTIME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.youtube").value(DEFAULT_YOUTUBE.toString()))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE.toString()));
    }

    @Test
    @Transactional
    public void getAllMoviesByImdbCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where imdbCode equals to DEFAULT_IMDB_CODE
        defaultMovieShouldBeFound("imdbCode.equals=" + DEFAULT_IMDB_CODE);

        // Get all the movieList where imdbCode equals to UPDATED_IMDB_CODE
        defaultMovieShouldNotBeFound("imdbCode.equals=" + UPDATED_IMDB_CODE);
    }

    @Test
    @Transactional
    public void getAllMoviesByImdbCodeIsInShouldWork() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where imdbCode in DEFAULT_IMDB_CODE or UPDATED_IMDB_CODE
        defaultMovieShouldBeFound("imdbCode.in=" + DEFAULT_IMDB_CODE + "," + UPDATED_IMDB_CODE);

        // Get all the movieList where imdbCode equals to UPDATED_IMDB_CODE
        defaultMovieShouldNotBeFound("imdbCode.in=" + UPDATED_IMDB_CODE);
    }

    @Test
    @Transactional
    public void getAllMoviesByImdbCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where imdbCode is not null
        defaultMovieShouldBeFound("imdbCode.specified=true");

        // Get all the movieList where imdbCode is null
        defaultMovieShouldNotBeFound("imdbCode.specified=false");
    }

    @Test
    @Transactional
    public void getAllMoviesByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where title equals to DEFAULT_TITLE
        defaultMovieShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the movieList where title equals to UPDATED_TITLE
        defaultMovieShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllMoviesByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultMovieShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the movieList where title equals to UPDATED_TITLE
        defaultMovieShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllMoviesByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where title is not null
        defaultMovieShouldBeFound("title.specified=true");

        // Get all the movieList where title is null
        defaultMovieShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    public void getAllMoviesBySlugIsEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where slug equals to DEFAULT_SLUG
        defaultMovieShouldBeFound("slug.equals=" + DEFAULT_SLUG);

        // Get all the movieList where slug equals to UPDATED_SLUG
        defaultMovieShouldNotBeFound("slug.equals=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    public void getAllMoviesBySlugIsInShouldWork() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where slug in DEFAULT_SLUG or UPDATED_SLUG
        defaultMovieShouldBeFound("slug.in=" + DEFAULT_SLUG + "," + UPDATED_SLUG);

        // Get all the movieList where slug equals to UPDATED_SLUG
        defaultMovieShouldNotBeFound("slug.in=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    public void getAllMoviesBySlugIsNullOrNotNull() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where slug is not null
        defaultMovieShouldBeFound("slug.specified=true");

        // Get all the movieList where slug is null
        defaultMovieShouldNotBeFound("slug.specified=false");
    }

    @Test
    @Transactional
    public void getAllMoviesByYearIsEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where year equals to DEFAULT_YEAR
        defaultMovieShouldBeFound("year.equals=" + DEFAULT_YEAR);

        // Get all the movieList where year equals to UPDATED_YEAR
        defaultMovieShouldNotBeFound("year.equals=" + UPDATED_YEAR);
    }

    @Test
    @Transactional
    public void getAllMoviesByYearIsInShouldWork() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where year in DEFAULT_YEAR or UPDATED_YEAR
        defaultMovieShouldBeFound("year.in=" + DEFAULT_YEAR + "," + UPDATED_YEAR);

        // Get all the movieList where year equals to UPDATED_YEAR
        defaultMovieShouldNotBeFound("year.in=" + UPDATED_YEAR);
    }

    @Test
    @Transactional
    public void getAllMoviesByYearIsNullOrNotNull() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where year is not null
        defaultMovieShouldBeFound("year.specified=true");

        // Get all the movieList where year is null
        defaultMovieShouldNotBeFound("year.specified=false");
    }

    @Test
    @Transactional
    public void getAllMoviesByYearIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where year greater than or equals to DEFAULT_YEAR
        defaultMovieShouldBeFound("year.greaterOrEqualThan=" + DEFAULT_YEAR);

        // Get all the movieList where year greater than or equals to UPDATED_YEAR
        defaultMovieShouldNotBeFound("year.greaterOrEqualThan=" + UPDATED_YEAR);
    }

    @Test
    @Transactional
    public void getAllMoviesByYearIsLessThanSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where year less than or equals to DEFAULT_YEAR
        defaultMovieShouldNotBeFound("year.lessThan=" + DEFAULT_YEAR);

        // Get all the movieList where year less than or equals to UPDATED_YEAR
        defaultMovieShouldBeFound("year.lessThan=" + UPDATED_YEAR);
    }


    @Test
    @Transactional
    public void getAllMoviesByRatingIsEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where rating equals to DEFAULT_RATING
        defaultMovieShouldBeFound("rating.equals=" + DEFAULT_RATING);

        // Get all the movieList where rating equals to UPDATED_RATING
        defaultMovieShouldNotBeFound("rating.equals=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    public void getAllMoviesByRatingIsInShouldWork() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where rating in DEFAULT_RATING or UPDATED_RATING
        defaultMovieShouldBeFound("rating.in=" + DEFAULT_RATING + "," + UPDATED_RATING);

        // Get all the movieList where rating equals to UPDATED_RATING
        defaultMovieShouldNotBeFound("rating.in=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    public void getAllMoviesByRatingIsNullOrNotNull() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where rating is not null
        defaultMovieShouldBeFound("rating.specified=true");

        // Get all the movieList where rating is null
        defaultMovieShouldNotBeFound("rating.specified=false");
    }

    @Test
    @Transactional
    public void getAllMoviesByRuntimeIsEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where runtime equals to DEFAULT_RUNTIME
        defaultMovieShouldBeFound("runtime.equals=" + DEFAULT_RUNTIME);

        // Get all the movieList where runtime equals to UPDATED_RUNTIME
        defaultMovieShouldNotBeFound("runtime.equals=" + UPDATED_RUNTIME);
    }

    @Test
    @Transactional
    public void getAllMoviesByRuntimeIsInShouldWork() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where runtime in DEFAULT_RUNTIME or UPDATED_RUNTIME
        defaultMovieShouldBeFound("runtime.in=" + DEFAULT_RUNTIME + "," + UPDATED_RUNTIME);

        // Get all the movieList where runtime equals to UPDATED_RUNTIME
        defaultMovieShouldNotBeFound("runtime.in=" + UPDATED_RUNTIME);
    }

    @Test
    @Transactional
    public void getAllMoviesByRuntimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where runtime is not null
        defaultMovieShouldBeFound("runtime.specified=true");

        // Get all the movieList where runtime is null
        defaultMovieShouldNotBeFound("runtime.specified=false");
    }

    @Test
    @Transactional
    public void getAllMoviesByRuntimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where runtime greater than or equals to DEFAULT_RUNTIME
        defaultMovieShouldBeFound("runtime.greaterOrEqualThan=" + DEFAULT_RUNTIME);

        // Get all the movieList where runtime greater than or equals to UPDATED_RUNTIME
        defaultMovieShouldNotBeFound("runtime.greaterOrEqualThan=" + UPDATED_RUNTIME);
    }

    @Test
    @Transactional
    public void getAllMoviesByRuntimeIsLessThanSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where runtime less than or equals to DEFAULT_RUNTIME
        defaultMovieShouldNotBeFound("runtime.lessThan=" + DEFAULT_RUNTIME);

        // Get all the movieList where runtime less than or equals to UPDATED_RUNTIME
        defaultMovieShouldBeFound("runtime.lessThan=" + UPDATED_RUNTIME);
    }


    @Test
    @Transactional
    public void getAllMoviesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where description equals to DEFAULT_DESCRIPTION
        defaultMovieShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the movieList where description equals to UPDATED_DESCRIPTION
        defaultMovieShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllMoviesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultMovieShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the movieList where description equals to UPDATED_DESCRIPTION
        defaultMovieShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllMoviesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where description is not null
        defaultMovieShouldBeFound("description.specified=true");

        // Get all the movieList where description is null
        defaultMovieShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllMoviesByYoutubeIsEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where youtube equals to DEFAULT_YOUTUBE
        defaultMovieShouldBeFound("youtube.equals=" + DEFAULT_YOUTUBE);

        // Get all the movieList where youtube equals to UPDATED_YOUTUBE
        defaultMovieShouldNotBeFound("youtube.equals=" + UPDATED_YOUTUBE);
    }

    @Test
    @Transactional
    public void getAllMoviesByYoutubeIsInShouldWork() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where youtube in DEFAULT_YOUTUBE or UPDATED_YOUTUBE
        defaultMovieShouldBeFound("youtube.in=" + DEFAULT_YOUTUBE + "," + UPDATED_YOUTUBE);

        // Get all the movieList where youtube equals to UPDATED_YOUTUBE
        defaultMovieShouldNotBeFound("youtube.in=" + UPDATED_YOUTUBE);
    }

    @Test
    @Transactional
    public void getAllMoviesByYoutubeIsNullOrNotNull() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where youtube is not null
        defaultMovieShouldBeFound("youtube.specified=true");

        // Get all the movieList where youtube is null
        defaultMovieShouldNotBeFound("youtube.specified=false");
    }

    @Test
    @Transactional
    public void getAllMoviesByLanguageIsEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where language equals to DEFAULT_LANGUAGE
        defaultMovieShouldBeFound("language.equals=" + DEFAULT_LANGUAGE);

        // Get all the movieList where language equals to UPDATED_LANGUAGE
        defaultMovieShouldNotBeFound("language.equals=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    public void getAllMoviesByLanguageIsInShouldWork() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where language in DEFAULT_LANGUAGE or UPDATED_LANGUAGE
        defaultMovieShouldBeFound("language.in=" + DEFAULT_LANGUAGE + "," + UPDATED_LANGUAGE);

        // Get all the movieList where language equals to UPDATED_LANGUAGE
        defaultMovieShouldNotBeFound("language.in=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    public void getAllMoviesByLanguageIsNullOrNotNull() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where language is not null
        defaultMovieShouldBeFound("language.specified=true");

        // Get all the movieList where language is null
        defaultMovieShouldNotBeFound("language.specified=false");
    }

    @Test
    @Transactional
    public void getAllMoviesByCastIsEqualToSomething() throws Exception {
        // Initialize the database
        Cast cast = CastResourceIntTest.createEntity(em);
        em.persist(cast);
        em.flush();
        movie.addCast(cast);
        movieRepository.saveAndFlush(movie);
        Long castId = cast.getId();

        // Get all the movieList where cast equals to castId
        defaultMovieShouldBeFound("castId.equals=" + castId);

        // Get all the movieList where cast equals to castId + 1
        defaultMovieShouldNotBeFound("castId.equals=" + (castId + 1));
    }


    @Test
    @Transactional
    public void getAllMoviesByImagesIsEqualToSomething() throws Exception {
        // Initialize the database
        Image images = ImageResourceIntTest.createEntity(em);
        em.persist(images);
        em.flush();
        movie.addImages(images);
        movieRepository.saveAndFlush(movie);
        Long imagesId = images.getId();

        // Get all the movieList where images equals to imagesId
        defaultMovieShouldBeFound("imagesId.equals=" + imagesId);

        // Get all the movieList where images equals to imagesId + 1
        defaultMovieShouldNotBeFound("imagesId.equals=" + (imagesId + 1));
    }


    @Test
    @Transactional
    public void getAllMoviesByTorrentsIsEqualToSomething() throws Exception {
        // Initialize the database
        Torrent torrents = TorrentResourceIntTest.createEntity(em);
        em.persist(torrents);
        em.flush();
        movie.addTorrents(torrents);
        movieRepository.saveAndFlush(movie);
        Long torrentsId = torrents.getId();

        // Get all the movieList where torrents equals to torrentsId
        defaultMovieShouldBeFound("torrentsId.equals=" + torrentsId);

        // Get all the movieList where torrents equals to torrentsId + 1
        defaultMovieShouldNotBeFound("torrentsId.equals=" + (torrentsId + 1));
    }


    @Test
    @Transactional
    public void getAllMoviesByGenresIsEqualToSomething() throws Exception {
        // Initialize the database
        Genre genres = GenreResourceIntTest.createEntity(em);
        em.persist(genres);
        em.flush();
        movie.addGenres(genres);
        movieRepository.saveAndFlush(movie);
        Long genresId = genres.getId();

        // Get all the movieList where genres equals to genresId
        defaultMovieShouldBeFound("genresId.equals=" + genresId);

        // Get all the movieList where genres equals to genresId + 1
        defaultMovieShouldNotBeFound("genresId.equals=" + (genresId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultMovieShouldBeFound(String filter) throws Exception {
        restMovieMockMvc.perform(get("/api/movies?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(movie.getId().intValue())))
            .andExpect(jsonPath("$.[*].imdbCode").value(hasItem(DEFAULT_IMDB_CODE.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].slug").value(hasItem(DEFAULT_SLUG.toString())))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING.doubleValue())))
            .andExpect(jsonPath("$.[*].runtime").value(hasItem(DEFAULT_RUNTIME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].youtube").value(hasItem(DEFAULT_YOUTUBE.toString())))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())));

        // Check, that the count call also returns 1
        restMovieMockMvc.perform(get("/api/movies/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultMovieShouldNotBeFound(String filter) throws Exception {
        restMovieMockMvc.perform(get("/api/movies?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMovieMockMvc.perform(get("/api/movies/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingMovie() throws Exception {
        // Get the movie
        restMovieMockMvc.perform(get("/api/movies/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMovie() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        int databaseSizeBeforeUpdate = movieRepository.findAll().size();

        // Update the movie
        Movie updatedMovie = movieRepository.findById(movie.getId()).get();
        // Disconnect from session so that the updates on updatedMovie are not directly saved in db
        em.detach(updatedMovie);
        updatedMovie
            .imdbCode(UPDATED_IMDB_CODE)
            .title(UPDATED_TITLE)
            .slug(UPDATED_SLUG)
            .year(UPDATED_YEAR)
            .rating(UPDATED_RATING)
            .runtime(UPDATED_RUNTIME)
            .description(UPDATED_DESCRIPTION)
            .youtube(UPDATED_YOUTUBE)
            .language(UPDATED_LANGUAGE);
        MovieDTO movieDTO = movieMapper.toDto(updatedMovie);

        restMovieMockMvc.perform(put("/api/movies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(movieDTO)))
            .andExpect(status().isOk());

        // Validate the Movie in the database
        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeUpdate);
        Movie testMovie = movieList.get(movieList.size() - 1);
        assertThat(testMovie.getImdbCode()).isEqualTo(UPDATED_IMDB_CODE);
        assertThat(testMovie.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testMovie.getSlug()).isEqualTo(UPDATED_SLUG);
        assertThat(testMovie.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testMovie.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testMovie.getRuntime()).isEqualTo(UPDATED_RUNTIME);
        assertThat(testMovie.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMovie.getYoutube()).isEqualTo(UPDATED_YOUTUBE);
        assertThat(testMovie.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    public void updateNonExistingMovie() throws Exception {
        int databaseSizeBeforeUpdate = movieRepository.findAll().size();

        // Create the Movie
        MovieDTO movieDTO = movieMapper.toDto(movie);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMovieMockMvc.perform(put("/api/movies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(movieDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Movie in the database
        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMovie() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        int databaseSizeBeforeDelete = movieRepository.findAll().size();

        // Get the movie
        restMovieMockMvc.perform(delete("/api/movies/{id}", movie.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Movie.class);
        Movie movie1 = new Movie();
        movie1.setId(1L);
        Movie movie2 = new Movie();
        movie2.setId(movie1.getId());
        assertThat(movie1).isEqualTo(movie2);
        movie2.setId(2L);
        assertThat(movie1).isNotEqualTo(movie2);
        movie1.setId(null);
        assertThat(movie1).isNotEqualTo(movie2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MovieDTO.class);
        MovieDTO movieDTO1 = new MovieDTO();
        movieDTO1.setId(1L);
        MovieDTO movieDTO2 = new MovieDTO();
        assertThat(movieDTO1).isNotEqualTo(movieDTO2);
        movieDTO2.setId(movieDTO1.getId());
        assertThat(movieDTO1).isEqualTo(movieDTO2);
        movieDTO2.setId(2L);
        assertThat(movieDTO1).isNotEqualTo(movieDTO2);
        movieDTO1.setId(null);
        assertThat(movieDTO1).isNotEqualTo(movieDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(movieMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(movieMapper.fromId(null)).isNull();
    }
}
