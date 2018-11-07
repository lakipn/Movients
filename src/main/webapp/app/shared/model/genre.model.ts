import { IMovie } from 'app/shared/model//movie.model';

export interface IGenre {
    id?: number;
    name?: string;
    movies?: IMovie[];
}

export class Genre implements IGenre {
    constructor(public id?: number, public name?: string, public movies?: IMovie[]) {}
}
