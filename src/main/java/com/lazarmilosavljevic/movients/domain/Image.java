package com.lazarmilosavljevic.movients.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

import com.lazarmilosavljevic.movients.domain.enumeration.ImageType;

/**
 * A Image.
 */
@Entity
@Table(name = "image")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Image implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 1024)
    @Column(name = "medium", length = 1024)
    private String medium;

    @Size(max = 1024)
    @Column(name = "large", length = 1024)
    private String large;

    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_type")
    private ImageType type;

    @ManyToOne
    @JsonIgnoreProperties("images")
    private Movie movie;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMedium() {
        return medium;
    }

    public Image medium(String medium) {
        this.medium = medium;
        return this;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getLarge() {
        return large;
    }

    public Image large(String large) {
        this.large = large;
        return this;
    }

    public void setLarge(String large) {
        this.large = large;
    }

    public ImageType getType() {
        return type;
    }

    public Image type(ImageType type) {
        this.type = type;
        return this;
    }

    public void setType(ImageType type) {
        this.type = type;
    }

    public Movie getMovie() {
        return movie;
    }

    public Image movie(Movie movie) {
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
        Image image = (Image) o;
        if (image.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), image.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Image{" +
            "id=" + getId() +
            ", medium='" + getMedium() + "'" +
            ", large='" + getLarge() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
