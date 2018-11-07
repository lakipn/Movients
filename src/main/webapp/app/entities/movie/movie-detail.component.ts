import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMovie } from 'app/shared/model/movie.model';
import { IImage } from 'app/shared/model/image.model';
import { IGenre } from 'app/shared/model/genre.model';

@Component({
    selector: 'jhi-movie-detail',
    templateUrl: './movie-detail.component.html'
})
export class MovieDetailComponent implements OnInit {
    movie: IMovie;
    cover: IImage;
    screenshots: IImage[];

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ movie }) => {
            this.movie = movie;
            this.cover = this.getCover(movie.images);
            this.screenshots = this.getScreenshots(movie.images);
        });
    }

    previousState() {
        window.history.back();
    }

    private getCover(images: IImage[]) {
        for (const img of images) {
            if (img.type === 'COVER') {
                return img;
            }
        }
        return images[0];
    }

    private joinGenres(genres: IGenre[]) {
        const objs = [];
        for (const genre of genres) {
            objs.push(genre.name);
        }
        return objs.join(', ');
    }

    private getScreenshots(images: IImage[]) {
        const ret = [];
        for (const img of images) {
            if (img.type === 'SCREENSHOT') {
                ret.push(img);
            }
        }
        return ret;
    }
}
