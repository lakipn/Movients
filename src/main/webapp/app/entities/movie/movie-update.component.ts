import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IMovie } from 'app/shared/model/movie.model';
import { MovieService } from './movie.service';
import { IGenre } from 'app/shared/model/genre.model';
import { GenreService } from 'app/entities/genre';

@Component({
    selector: 'jhi-movie-update',
    templateUrl: './movie-update.component.html'
})
export class MovieUpdateComponent implements OnInit {
    movie: IMovie;
    isSaving: boolean;

    genres: IGenre[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private movieService: MovieService,
        private genreService: GenreService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ movie }) => {
            this.movie = movie;
        });
        this.genreService.query().subscribe(
            (res: HttpResponse<IGenre[]>) => {
                this.genres = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.movie.id !== undefined) {
            this.subscribeToSaveResponse(this.movieService.update(this.movie));
        } else {
            this.subscribeToSaveResponse(this.movieService.create(this.movie));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IMovie>>) {
        result.subscribe((res: HttpResponse<IMovie>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackGenreById(index: number, item: IGenre) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}
