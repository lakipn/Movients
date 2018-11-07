import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { ITorrent } from 'app/shared/model/torrent.model';
import { TorrentService } from './torrent.service';
import { IMovie } from 'app/shared/model/movie.model';
import { MovieService } from 'app/entities/movie';

@Component({
    selector: 'jhi-torrent-update',
    templateUrl: './torrent-update.component.html'
})
export class TorrentUpdateComponent implements OnInit {
    torrent: ITorrent;
    isSaving: boolean;

    movies: IMovie[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private torrentService: TorrentService,
        private movieService: MovieService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ torrent }) => {
            this.torrent = torrent;
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
        if (this.torrent.id !== undefined) {
            this.subscribeToSaveResponse(this.torrentService.update(this.torrent));
        } else {
            this.subscribeToSaveResponse(this.torrentService.create(this.torrent));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ITorrent>>) {
        result.subscribe((res: HttpResponse<ITorrent>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
