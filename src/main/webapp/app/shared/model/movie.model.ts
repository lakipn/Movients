import { ICast } from 'app/shared/model//cast.model';
import { IImage } from 'app/shared/model//image.model';
import { ITorrent } from 'app/shared/model//torrent.model';
import { IGenre } from 'app/shared/model//genre.model';

export interface IMovie {
    id?: number;
    imdbCode?: string;
    title?: string;
    slug?: string;
    year?: number;
    rating?: number;
    runtime?: number;
    description?: string;
    youtube?: string;
    language?: string;
    casts?: ICast[];
    images?: IImage[];
    torrents?: ITorrent[];
    genres?: IGenre[];
}

export class Movie implements IMovie {
    constructor(
        public id?: number,
        public imdbCode?: string,
        public title?: string,
        public slug?: string,
        public year?: number,
        public rating?: number,
        public runtime?: number,
        public description?: string,
        public youtube?: string,
        public language?: string,
        public casts?: ICast[],
        public images?: IImage[],
        public torrents?: ITorrent[],
        public genres?: IGenre[]
    ) {}
}
