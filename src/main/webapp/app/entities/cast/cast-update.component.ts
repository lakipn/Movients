import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { ICast } from 'app/shared/model/cast.model';
import { CastService } from './cast.service';
import { IMovie } from 'app/shared/model/movie.model';
import { MovieService } from 'app/entities/movie';

@Component({
    selector: 'jhi-cast-update',
    templateUrl: './cast-update.component.html'
})
export class CastUpdateComponent implements OnInit {
    cast: ICast;
    isSaving: boolean;

    movies: IMovie[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private castService: CastService,
        private movieService: MovieService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ cast }) => {
            this.cast = cast;
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
        if (this.cast.id !== undefined) {
            this.subscribeToSaveResponse(this.castService.update(this.cast));
        } else {
            this.subscribeToSaveResponse(this.castService.create(this.cast));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ICast>>) {
        result.subscribe((res: HttpResponse<ICast>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
}
