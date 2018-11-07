export interface ITorrent {
    id?: number;
    url?: string;
    hash?: string;
    quality?: string;
    size?: string;
    movieTitle?: string;
    movieId?: number;
}

export class Torrent implements ITorrent {
    constructor(
        public id?: number,
        public url?: string,
        public hash?: string,
        public quality?: string,
        public size?: string,
        public movieTitle?: string,
        public movieId?: number
    ) {}
}
