package com.lazarmilosavljevic.movients.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Movie.
 */
@Entity
@Table(name = "movie")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Movie implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "imdb_code")
    private String imdbCode;

    @Column(name = "title")
    private String title;

    @Column(name = "slug")
    private String slug;

    @Column(name = "jhi_year")
    private Integer year;

    @Column(name = "rating")
    private Double rating;

    @Column(name = "runtime")
    private Integer runtime;

    @Size(max = 2048)
    @Column(name = "description", length = 2048)
    private String description;

    @Size(max = 512)
    @Column(name = "youtube", length = 512)
    private String youtube;

    @Column(name = "language")
    private String language;

    @OneToMany(mappedBy = "movie")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Cast> casts = new HashSet<>();
    @OneToMany(mappedBy = "movie")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Image> images = new HashSet<>();
    @OneToMany(mappedBy = "movie")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Torrent> torrents = new HashSet<>();
    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @NotNull
    @JoinTable(name = "movie_genres",
               joinColumns = @JoinColumn(name = "movies_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "genres_id", referencedColumnName = "id"))
    private Set<Genre> genres = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImdbCode() {
        return imdbCode;
    }

    public Movie imdbCode(String imdbCode) {
        this.imdbCode = imdbCode;
        return this;
    }

    public void setImdbCode(String imdbCode) {
        this.imdbCode = imdbCode;
    }

    public String getTitle() {
        return title;
    }

    public Movie title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlug() {
        return slug;
    }

    public Movie slug(String slug) {
        this.slug = slug;
        return this;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Integer getYear() {
        return year;
    }

    public Movie year(Integer year) {
        this.year = year;
        return this;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Double getRating() {
        return rating;
    }

    public Movie rating(Double rating) {
        this.rating = rating;
        return this;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Integer getRuntime() {
        return runtime;
    }

    public Movie runtime(Integer runtime) {
        this.runtime = runtime;
        return this;
    }

    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }

    public String getDescription() {
        return description;
    }

    public Movie description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getYoutube() {
        return youtube;
    }

    public Movie youtube(String youtube) {
        this.youtube = youtube;
        return this;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    public String getLanguage() {
        return language;
    }

    public Movie language(String language) {
        this.language = language;
        return this;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Set<Cast> getCasts() {
        return casts;
    }

    public Movie casts(Set<Cast> casts) {
        this.casts = casts;
        return this;
    }

    public Movie addCast(Cast cast) {
        this.casts.add(cast);
        cast.setMovie(this);
        return this;
    }

    public Movie removeCast(Cast cast) {
        this.casts.remove(cast);
        cast.setMovie(null);
        return this;
    }

    public void setCasts(Set<Cast> casts) {
        this.casts = casts;
    }

    public Set<Image> getImages() {
        return images;
    }

    public Movie images(Set<Image> images) {
        this.images = images;
        return this;
    }

    public Movie addImages(Image image) {
        this.images.add(image);
        image.setMovie(this);
        return this;
    }

    public Movie removeImages(Image image) {
        this.images.remove(image);
        image.setMovie(null);
        return this;
    }

    public void setImages(Set<Image> images) {
        this.images = images;
    }

    public Set<Torrent> getTorrents() {
        return torrents;
    }

    public Movie torrents(Set<Torrent> torrents) {
        this.torrents = torrents;
        return this;
    }

    public Movie addTorrents(Torrent torrent) {
        this.torrents.add(torrent);
        torrent.setMovie(this);
        return this;
    }

    public Movie removeTorrents(Torrent torrent) {
        this.torrents.remove(torrent);
        torrent.setMovie(null);
        return this;
    }

    public void setTorrents(Set<Torrent> torrents) {
        this.torrents = torrents;
    }

    public Set<Genre> getGenres() {
        return genres;
    }

    public Movie genres(Set<Genre> genres) {
        this.genres = genres;
        return this;
    }

    public Movie addGenres(Genre genre) {
        this.genres.add(genre);
        genre.getMovies().add(this);
        return this;
    }

    public Movie removeGenres(Genre genre) {
        this.genres.remove(genre);
        genre.getMovies().remove(this);
        return this;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Movie movie = (Movie) o;
        if (movie.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), movie.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Movie{" +
            "id=" + getId() +
            ", imdbCode='" + getImdbCode() + "'" +
            ", title='" + getTitle() + "'" +
            ", slug='" + getSlug() + "'" +
            ", year=" + getYear() +
            ", rating=" + getRating() +
            ", runtime=" + getRuntime() +
            ", description='" + getDescription() + "'" +
            ", youtube='" + getYoutube() + "'" +
            ", language='" + getLanguage() + "'" +
            "}";
    }
}
