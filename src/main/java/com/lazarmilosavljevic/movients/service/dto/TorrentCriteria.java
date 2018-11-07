package com.lazarmilosavljevic.movients.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the Torrent entity. This class is used in TorrentResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /torrents?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TorrentCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter url;

    private StringFilter hash;

    private StringFilter quality;

    private StringFilter size;

    private LongFilter movieId;

    public TorrentCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getUrl() {
        return url;
    }

    public void setUrl(StringFilter url) {
        this.url = url;
    }

    public StringFilter getHash() {
        return hash;
    }

    public void setHash(StringFilter hash) {
        this.hash = hash;
    }

    public StringFilter getQuality() {
        return quality;
    }

    public void setQuality(StringFilter quality) {
        this.quality = quality;
    }

    public StringFilter getSize() {
        return size;
    }

    public void setSize(StringFilter size) {
        this.size = size;
    }

    public LongFilter getMovieId() {
        return movieId;
    }

    public void setMovieId(LongFilter movieId) {
        this.movieId = movieId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TorrentCriteria that = (TorrentCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(url, that.url) &&
            Objects.equals(hash, that.hash) &&
            Objects.equals(quality, that.quality) &&
            Objects.equals(size, that.size) &&
            Objects.equals(movieId, that.movieId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        url,
        hash,
        quality,
        size,
        movieId
        );
    }

    @Override
    public String toString() {
        return "TorrentCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (url != null ? "url=" + url + ", " : "") +
                (hash != null ? "hash=" + hash + ", " : "") +
                (quality != null ? "quality=" + quality + ", " : "") +
                (size != null ? "size=" + size + ", " : "") +
                (movieId != null ? "movieId=" + movieId + ", " : "") +
            "}";
    }

}
