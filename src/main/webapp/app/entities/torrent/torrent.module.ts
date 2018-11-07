import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MovientsSharedModule } from 'app/shared';
import {
    TorrentComponent,
    TorrentDetailComponent,
    TorrentUpdateComponent,
    TorrentDeletePopupComponent,
    TorrentDeleteDialogComponent,
    torrentRoute,
    torrentPopupRoute
} from './';

const ENTITY_STATES = [...torrentRoute, ...torrentPopupRoute];

@NgModule({
    imports: [MovientsSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        TorrentComponent,
        TorrentDetailComponent,
        TorrentUpdateComponent,
        TorrentDeleteDialogComponent,
        TorrentDeletePopupComponent
    ],
    entryComponents: [TorrentComponent, TorrentUpdateComponent, TorrentDeleteDialogComponent, TorrentDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MovientsTorrentModule {}
