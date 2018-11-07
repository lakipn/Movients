import { Component, OnDestroy, OnInit } from '@angular/core';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiAlertService, JhiEventManager, JhiParseLinks } from 'ng-jhipster';

import { Account, LoginModalService, Principal } from 'app/core';
import { MovieService } from 'app/entities/movie';
import { IMovie } from 'app/shared/model/movie.model';
import { Subscription } from 'rxjs';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { ITEMS_PER_PAGE } from 'app/shared';
import { IImage } from 'app/shared/model/image.model';
import { retry } from 'rxjs/operators';

@Component({
    selector: 'jhi-home',
    templateUrl: './home.component.html',
    styleUrls: ['home.scss']
})
export class HomeComponent implements OnInit, OnDestroy {
    account: Account;
    modalRef: NgbModalRef;
    movies: IMovie[];
    error: any;
    success: any;
    eventSubscriber: Subscription;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;

    constructor(
        private movieService: MovieService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private loginModalService: LoginModalService,
        private eventManager: JhiEventManager
    ) {
        this.page = 1;
        this.previousPage = 0;
        this.reverse = true;
        this.predicate = 'id';
        this.itemsPerPage = ITEMS_PER_PAGE + 4;
        this.routeData = this.activatedRoute.data.subscribe(data => {
            if (!!data.pagingParams) {
                this.page = data.pagingParams.page;
                this.previousPage = data.pagingParams.page;
                this.reverse = data.pagingParams.ascending;
                this.predicate = data.pagingParams.predicate;
            }
        });
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.account = account;
        });
        this.registerAuthenticationSuccess();
        this.registerChangeInMovies();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe('authenticationSuccess', message => {
            this.principal.identity().then(account => {
                this.account = account;
            });
        });
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    login() {
        this.modalRef = this.loginModalService.open();
    }

    loadAll(term?: string) {
        const req = {
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort()
        };
        if (!!term && term.length >= 3) {
            req['title.contains'] = term;
        }
        this.movieService
            .query(req)
            .pipe(retry(3))
            .subscribe(
                (res: HttpResponse<IMovie[]>) => this.paginateMovies(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    transition() {
        this.router.navigate(['/home'], {
            queryParams: {
                page: this.page,
                size: this.itemsPerPage,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll();
    }

    clear(term?: string) {
        this.page = 0;
        this.router.navigate([
            '/home',
            {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        if (!!term && term.length >= 3) {
            this.loadAll(term);
        } else {
            this.loadAll();
        }
    }

    trackId(index: number, item: IMovie) {
        return item.id;
    }

    registerChangeInMovies() {
        this.eventSubscriber = this.eventManager.subscribe('movieListModification', response => this.loadAll());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private paginateMovies(data: IMovie[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.movies = data;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    private getCover(images: IImage[]) {
        for (const img of images) {
            if (img.type === 'COVER') {
                return img;
            }
        }
        return images[0];
    }

    private lookitup(term: string) {
        console.log('Looking for: ' + term);
        this.clear(term);
    }
}
