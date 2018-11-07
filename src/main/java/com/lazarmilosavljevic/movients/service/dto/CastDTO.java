package com.lazarmilosavljevic.movients.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Cast entity.
 */
public class CastDTO implements Serializable {

    private Long id;

    private String name;

    private String characterName;

    @Size(max = 1024)
    private String image;

    @Size(max = 512)
    private String imdb;

    private Long movieId;

    private String movieTitle;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImdb() {
        return imdb;
    }

    public void setImdb(String imdb) {
        this.imdb = imdb;
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CastDTO castDTO = (CastDTO) o;
        if (castDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), castDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CastDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", characterName='" + getCharacterName() + "'" +
            ", image='" + getImage() + "'" +
            ", imdb='" + getImdb() + "'" +
            ", movie=" + getMovieId() +
            ", movie='" + getMovieTitle() + "'" +
            "}";
    }
}
