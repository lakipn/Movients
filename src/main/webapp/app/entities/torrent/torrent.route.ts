import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Torrent } from 'app/shared/model/torrent.model';
import { TorrentService } from './torrent.service';
import { TorrentComponent } from './torrent.component';
import { TorrentDetailComponent } from './torrent-detail.component';
import { TorrentUpdateComponent } from './torrent-update.component';
import { TorrentDeletePopupComponent } from './torrent-delete-dialog.component';
import { ITorrent } from 'app/shared/model/torrent.model';

@Injectable({ providedIn: 'root' })
export class TorrentResolve implements Resolve<ITorrent> {
    constructor(private service: TorrentService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Torrent> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Torrent>) => response.ok),
                map((torrent: HttpResponse<Torrent>) => torrent.body)
            );
        }
        return of(new Torrent());
    }
}

export const torrentRoute: Routes = [
    {
        path: 'torrent',
        component: TorrentComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'movientsApp.torrent.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'torrent/:id/view',
        component: TorrentDetailComponent,
        resolve: {
            torrent: TorrentResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'movientsApp.torrent.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'torrent/new',
        component: TorrentUpdateComponent,
        resolve: {
            torrent: TorrentResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'movientsApp.torrent.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'torrent/:id/edit',
        component: TorrentUpdateComponent,
        resolve: {
            torrent: TorrentResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'movientsApp.torrent.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const torrentPopupRoute: Routes = [
    {
        path: 'torrent/:id/delete',
        component: TorrentDeletePopupComponent,
        resolve: {
            torrent: TorrentResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'movientsApp.torrent.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
