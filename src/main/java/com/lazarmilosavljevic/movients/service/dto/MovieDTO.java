package com.lazarmilosavljevic.movients.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Movie entity.
 */
public class MovieDTO implements Serializable {

    private Long id;

    private String imdbCode;

    private String title;

    private String slug;

    private Integer year;

    private Double rating;

    private Integer runtime;

    @Size(max = 2048)
    private String description;

    @Size(max = 512)
    private String youtube;

    private String language;

    private Set<GenreDTO> genres = new HashSet<>();

    private Set<CastDTO> casts = new HashSet<>();

    private Set<ImageDTO> images = new HashSet<>();

    private Set<TorrentDTO> torrents = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImdbCode() {
        return imdbCode;
    }

    public void setImdbCode(String imdbCode) {
        this.imdbCode = imdbCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Integer getRuntime() {
        return runtime;
    }

    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Set<GenreDTO> getGenres() {
        return genres;
    }

    public void setGenres(Set<GenreDTO> genres) {
        this.genres = genres;
    }

    public Set<CastDTO> getCasts() {
        return casts;
    }

    public void setCasts(Set<CastDTO> casts) {
        this.casts = casts;
    }

    public Set<ImageDTO> getImages() {
        return images;
    }

    public void setImages(Set<ImageDTO> images) {
        this.images = images;
    }

    public Set<TorrentDTO> getTorrents() {
        return torrents;
    }

    public void setTorrents(Set<TorrentDTO> torrents) {
        this.torrents = torrents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MovieDTO movieDTO = (MovieDTO) o;
        if (movieDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), movieDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MovieDTO{" +
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
