package com.lazarmilosavljevic.movients.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Torrent.
 */
@Entity
@Table(name = "torrent")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Torrent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 2048)
    @Column(name = "url", length = 2048)
    private String url;

    @Size(max = 2048)
    @Column(name = "jhi_hash", length = 2048)
    private String hash;

    @Column(name = "quality")
    private String quality;

    @Column(name = "jhi_size")
    private String size;

    @ManyToOne
    @JsonIgnoreProperties("torrents")
    private Movie movie;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public Torrent url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHash() {
        return hash;
    }

    public Torrent hash(String hash) {
        this.hash = hash;
        return this;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getQuality() {
        return quality;
    }

    public Torrent quality(String quality) {
        this.quality = quality;
        return this;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getSize() {
        return size;
    }

    public Torrent size(String size) {
        this.size = size;
        return this;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Movie getMovie() {
        return movie;
    }

    public Torrent movie(Movie movie) {
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
        Torrent torrent = (Torrent) o;
        if (torrent.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), torrent.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Torrent{" +
            "id=" + getId() +
            ", url='" + getUrl() + "'" +
            ", hash='" + getHash() + "'" +
            ", quality='" + getQuality() + "'" +
            ", size='" + getSize() + "'" +
            "}";
    }
}
