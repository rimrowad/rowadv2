import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Team } from './team.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Team>;

@Injectable()
export class TeamService {

    private resourceUrl =  SERVER_API_URL + 'api/teams';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(team: Team): Observable<EntityResponseType> {
        const copy = this.convert(team);
        return this.http.post<Team>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(team: Team): Observable<EntityResponseType> {
        const copy = this.convert(team);
        return this.http.put<Team>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Team>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Team[]>> {
        const options = createRequestOption(req);
        return this.http.get<Team[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Team[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Team = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Team[]>): HttpResponse<Team[]> {
        const jsonResponse: Team[] = res.body;
        const body: Team[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Team.
     */
    private convertItemFromServer(team: Team): Team {
        const copy: Team = Object.assign({}, team);
        copy.creationDate = this.dateUtils
            .convertLocalDateFromServer(team.creationDate);
        return copy;
    }

    /**
     * Convert a Team to a JSON which can be sent to the server.
     */
    private convert(team: Team): Team {
        const copy: Team = Object.assign({}, team);
        copy.creationDate = this.dateUtils
            .convertLocalDateToServer(team.creationDate);
        return copy;
    }
}
