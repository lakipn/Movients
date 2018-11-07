package com.lazarmilosavljevic.movients.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Cast.
 */
@Entity
@Table(name = "cast")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Cast implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "character_name")
    private String characterName;

    @Size(max = 1024)
    @Column(name = "image", length = 1024)
    private String image;

    @Size(max = 512)
    @Column(name = "imdb", length = 512)
    private String imdb;

    @ManyToOne
    @JsonIgnoreProperties("casts")
    private Movie movie;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Cast name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCharacterName() {
        return characterName;
    }

    public Cast characterName(String characterName) {
        this.characterName = characterName;
        return this;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public String getImage() {
        return image;
    }

    public Cast image(String image) {
        this.image = image;
        return this;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImdb() {
        return imdb;
    }

    public Cast imdb(String imdb) {
        this.imdb = imdb;
        return this;
    }

    public void setImdb(String imdb) {
        this.imdb = imdb;
    }

    public Movie getMovie() {
        return movie;
    }

    public Cast movie(Movie movie) {
        this.movie = movie;
        return this;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
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
        Cast cast = (Cast) o;
        if (cast.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cast.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Cast{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", characterName='" + getCharacterName() + "'" +
            ", image='" + getImage() + "'" +
            ", imdb='" + getImdb() + "'" +
            "}";
    }
}
