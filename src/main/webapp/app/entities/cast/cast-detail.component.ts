import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICast } from 'app/shared/model/cast.model';

@Component({
    selector: 'jhi-cast-detail',
    templateUrl: './cast-detail.component.html'
})
export class CastDetailComponent implements OnInit {
    cast: ICast;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ cast }) => {
            this.cast = cast;
        });
    }

    previousState() {
        window.history.back();
    }
}
