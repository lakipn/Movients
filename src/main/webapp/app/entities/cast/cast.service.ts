import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICast } from 'app/shared/model/cast.model';

type EntityResponseType = HttpResponse<ICast>;
type EntityArrayResponseType = HttpResponse<ICast[]>;

@Injectable({ providedIn: 'root' })
export class CastService {
    public resourceUrl = SERVER_API_URL + 'api/casts';

    constructor(private http: HttpClient) {}

    create(cast: ICast): Observable<EntityResponseType> {
        return this.http.post<ICast>(this.resourceUrl, cast, { observe: 'response' });
    }

    update(cast: ICast): Observable<EntityResponseType> {
        return this.http.put<ICast>(this.resourceUrl, cast, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ICast>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICast[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
