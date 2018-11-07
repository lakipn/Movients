import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { MovientsMovieModule } from './movie/movie.module';
import { MovientsCastModule } from './cast/cast.module';
import { MovientsGenreModule } from './genre/genre.module';
import { MovientsImageModule } from './image/image.module';
import { MovientsTorrentModule } from './torrent/torrent.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        MovientsMovieModule,
        MovientsCastModule,
        MovientsGenreModule,
        MovientsImageModule,
        MovientsTorrentModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MovientsEntityModule {}
