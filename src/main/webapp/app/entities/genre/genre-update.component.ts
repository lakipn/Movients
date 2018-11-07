import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IGenre } from 'app/shared/model/genre.model';
import { GenreService } from './genre.service';
import { IMovie } from 'app/shared/model/movie.model';
import { MovieService } from 'app/entities/movie';

@Component({
    selector: 'jhi-genre-update',
    templateUrl: './genre-update.component.html'
})
export class GenreUpdateComponent implements OnInit {
    genre: IGenre;
    isSaving: boolean;

    movies: IMovie[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private genreService: GenreService,
        private movieService: MovieService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ genre }) => {
            this.genre = genre;
        });
        this.movieService.query().subscribe(
            (res: HttpResponse<IMovie[]>) => {
                this.movies = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.genre.id !== undefined) {
            this.subscribeToSaveResponse(this.genreService.update(this.genre));
        } else {
            this.subscribeToSaveResponse(this.genreService.create(this.genre));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IGenre>>) {
        result.subscribe((res: HttpResponse<IGenre>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackMovieById(index: number, item: IMovie) {
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
