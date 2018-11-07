import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Cast } from 'app/shared/model/cast.model';
import { CastService } from './cast.service';
import { CastComponent } from './cast.component';
import { CastDetailComponent } from './cast-detail.component';
import { CastUpdateComponent } from './cast-update.component';
import { CastDeletePopupComponent } from './cast-delete-dialog.component';
import { ICast } from 'app/shared/model/cast.model';

@Injectable({ providedIn: 'root' })
export class CastResolve implements Resolve<ICast> {
    constructor(private service: CastService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Cast> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Cast>) => response.ok),
                map((cast: HttpResponse<Cast>) => cast.body)
            );
        }
        return of(new Cast());
    }
}

export const castRoute: Routes = [
    {
        path: 'cast',
        component: CastComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'movientsApp.cast.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'cast/:id/view',
        component: CastDetailComponent,
        resolve: {
            cast: CastResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'movientsApp.cast.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'cast/new',
        component: CastUpdateComponent,
        resolve: {
            cast: CastResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'movientsApp.cast.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'cast/:id/edit',
        component: CastUpdateComponent,
        resolve: {
            cast: CastResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'movientsApp.cast.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const castPopupRoute: Routes = [
    {
        path: 'cast/:id/delete',
        component: CastDeletePopupComponent,
        resolve: {
            cast: CastResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'movientsApp.cast.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
