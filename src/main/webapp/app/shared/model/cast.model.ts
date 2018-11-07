export interface ICast {
    id?: number;
    name?: string;
    characterName?: string;
    image?: string;
    imdb?: string;
    movieTitle?: string;
    movieId?: number;
}

export class Cast implements ICast {
    constructor(
        public id?: number,
        public name?: string,
        public characterName?: string,
        public image?: string,
        public imdb?: string,
        public movieTitle?: string,
        public movieId?: number
    ) {}
}
