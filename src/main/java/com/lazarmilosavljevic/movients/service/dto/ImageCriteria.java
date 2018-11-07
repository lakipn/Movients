package com.lazarmilosavljevic.movients.service.dto;

import java.io.Serializable;
import java.util.Objects;
import com.lazarmilosavljevic.movients.domain.enumeration.ImageType;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the Image entity. This class is used in ImageResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /images?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ImageCriteria implements Serializable {
    /**
     * Class for filtering ImageType
     */
    public static class ImageTypeFilter extends Filter<ImageType> {
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter medium;

    private StringFilter large;

    private ImageTypeFilter type;

    private LongFilter movieId;

    public ImageCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getMedium() {
        return medium;
    }

    public void setMedium(StringFilter medium) {
        this.medium = medium;
    }

    public StringFilter getLarge() {
        return large;
    }

    public void setLarge(StringFilter large) {
        this.large = large;
    }

    public ImageTypeFilter getType() {
        return type;
    }

    public void setType(ImageTypeFilter type) {
        this.type = type;
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
        final ImageCriteria that = (ImageCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(medium, that.medium) &&
            Objects.equals(large, that.large) &&
            Objects.equals(type, that.type) &&
            Objects.equals(movieId, that.movieId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        medium,
        large,
        type,
        movieId
        );
    }

    @Override
    public String toString() {
        return "ImageCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (medium != null ? "medium=" + medium + ", " : "") +
                (large != null ? "large=" + large + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (movieId != null ? "movieId=" + movieId + ", " : "") +
            "}";
    }

}
