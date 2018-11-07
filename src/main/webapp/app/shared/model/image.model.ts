export const enum ImageType {
    COVER = 'COVER',
    SCREENSHOT = 'SCREENSHOT'
}

export interface IImage {
    id?: number;
    medium?: string;
    large?: string;
    type?: ImageType;
    movieTitle?: string;
    movieId?: number;
}

export class Image implements IImage {
    constructor(
        public id?: number,
        public medium?: string,
        public large?: string,
        public type?: ImageType,
        public movieTitle?: string,
        public movieId?: number
    ) {}
}
