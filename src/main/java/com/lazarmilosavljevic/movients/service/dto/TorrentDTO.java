package com.lazarmilosavljevic.movients.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Torrent entity.
 */
public class TorrentDTO implements Serializable {

    private Long id;

    @Size(max = 2048)
    private String url;

    @Size(max = 2048)
    private String hash;

    private String quality;

    private String size;

    private Long movieId;

    private String movieTitle;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
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

        TorrentDTO torrentDTO = (TorrentDTO) o;
        if (torrentDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), torrentDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TorrentDTO{" +
            "id=" + getId() +
            ", url='" + getUrl() + "'" +
            ", hash='" + getHash() + "'" +
            ", quality='" + getQuality() + "'" +
            ", size='" + getSize() + "'" +
            ", movie=" + getMovieId() +
            ", movie='" + getMovieTitle() + "'" +
            "}";
    }
}
