import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MovientsSharedModule } from 'app/shared';
import {
    CastComponent,
    CastDetailComponent,
    CastUpdateComponent,
    CastDeletePopupComponent,
    CastDeleteDialogComponent,
    castRoute,
    castPopupRoute
} from './';

const ENTITY_STATES = [...castRoute, ...castPopupRoute];

@NgModule({
    imports: [MovientsSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [CastComponent, CastDetailComponent, CastUpdateComponent, CastDeleteDialogComponent, CastDeletePopupComponent],
    entryComponents: [CastComponent, CastUpdateComponent, CastDeleteDialogComponent, CastDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MovientsCastModule {}
