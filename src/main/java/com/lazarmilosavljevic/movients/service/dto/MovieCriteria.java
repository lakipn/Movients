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
 * Criteria class for the Movie entity. This class is used in MovieResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /movies?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MovieCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter imdbCode;

    private StringFilter title;

    private StringFilter slug;

    private IntegerFilter year;

    private DoubleFilter rating;

    private IntegerFilter runtime;

    private StringFilter description;

    private StringFilter youtube;

    private StringFilter language;

    private LongFilter castId;

    private LongFilter imagesId;

    private LongFilter torrentsId;

    private LongFilter genresId;

    public MovieCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getImdbCode() {
        return imdbCode;
    }

    public void setImdbCode(StringFilter imdbCode) {
        this.imdbCode = imdbCode;
    }

    public StringFilter getTitle() {
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getSlug() {
        return slug;
    }

    public void setSlug(StringFilter slug) {
        this.slug = slug;
    }

    public IntegerFilter getYear() {
        return year;
    }

    public void setYear(IntegerFilter year) {
        this.year = year;
    }

    public DoubleFilter getRating() {
        return rating;
    }

    public void setRating(DoubleFilter rating) {
        this.rating = rating;
    }

    public IntegerFilter getRuntime() {
        return runtime;
    }

    public void setRuntime(IntegerFilter runtime) {
        this.runtime = runtime;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getYoutube() {
        return youtube;
    }

    public void setYoutube(StringFilter youtube) {
        this.youtube = youtube;
    }

    public StringFilter getLanguage() {
        return language;
    }

    public void setLanguage(StringFilter language) {
        this.language = language;
    }

    public LongFilter getCastId() {
        return castId;
    }

    public void setCastId(LongFilter castId) {
        this.castId = castId;
    }

    public LongFilter getImagesId() {
        return imagesId;
    }

    public void setImagesId(LongFilter imagesId) {
        this.imagesId = imagesId;
    }

    public LongFilter getTorrentsId() {
        return torrentsId;
    }

    public void setTorrentsId(LongFilter torrentsId) {
        this.torrentsId = torrentsId;
    }

    public LongFilter getGenresId() {
        return genresId;
    }

    public void setGenresId(LongFilter genresId) {
        this.genresId = genresId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MovieCriteria that = (MovieCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(imdbCode, that.imdbCode) &&
            Objects.equals(title, that.title) &&
            Objects.equals(slug, that.slug) &&
            Objects.equals(year, that.year) &&
            Objects.equals(rating, that.rating) &&
            Objects.equals(runtime, that.runtime) &&
            Objects.equals(description, that.description) &&
            Objects.equals(youtube, that.youtube) &&
            Objects.equals(language, that.language) &&
            Objects.equals(castId, that.castId) &&
            Objects.equals(imagesId, that.imagesId) &&
            Objects.equals(torrentsId, that.torrentsId) &&
            Objects.equals(genresId, that.genresId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        imdbCode,
        title,
        slug,
        year,
        rating,
        runtime,
        description,
        youtube,
        language,
        castId,
        imagesId,
        torrentsId,
        genresId
        );
    }

    @Override
    public String toString() {
        return "MovieCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (imdbCode != null ? "imdbCode=" + imdbCode + ", " : "") +
                (title != null ? "title=" + title + ", " : "") +
                (slug != null ? "slug=" + slug + ", " : "") +
                (year != null ? "year=" + year + ", " : "") +
                (rating != null ? "rating=" + rating + ", " : "") +
                (runtime != null ? "runtime=" + runtime + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (youtube != null ? "youtube=" + youtube + ", " : "") +
                (language != null ? "language=" + language + ", " : "") +
                (castId != null ? "castId=" + castId + ", " : "") +
                (imagesId != null ? "imagesId=" + imagesId + ", " : "") +
                (torrentsId != null ? "torrentsId=" + torrentsId + ", " : "") +
                (genresId != null ? "genresId=" + genresId + ", " : "") +
            "}";
    }

}
