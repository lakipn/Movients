import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Movie } from 'app/shared/model/movie.model';
import { MovieService } from './movie.service';
import { MovieComponent } from './movie.component';
import { MovieDetailComponent } from './movie-detail.component';
import { MovieUpdateComponent } from './movie-update.component';
import { MovieDeletePopupComponent } from './movie-delete-dialog.component';
import { IMovie } from 'app/shared/model/movie.model';

@Injectable({ providedIn: 'root' })
export class MovieResolve implements Resolve<IMovie> {
    constructor(private service: MovieService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Movie> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Movie>) => response.ok),
                map((movie: HttpResponse<Movie>) => movie.body)
            );
        }
        return of(new Movie());
    }
}

export const movieRoute: Routes = [
    {
        path: 'movie',
        component: MovieComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'movientsApp.movie.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'movie/:id/view',
        component: MovieDetailComponent,
        resolve: {
            movie: MovieResolve
        },
        data: {
            pageTitle: 'movientsApp.movie.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'movie/new',
        component: MovieUpdateComponent,
        resolve: {
            movie: MovieResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'movientsApp.movie.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'movie/:id/edit',
        component: MovieUpdateComponent,
        resolve: {
            movie: MovieResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'movientsApp.movie.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const moviePopupRoute: Routes = [
    {
        path: 'movie/:id/delete',
        component: MovieDeletePopupComponent,
        resolve: {
            movie: MovieResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'movientsApp.movie.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
