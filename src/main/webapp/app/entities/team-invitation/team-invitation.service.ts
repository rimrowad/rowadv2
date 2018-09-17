import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { TeamInvitation } from './team-invitation.model';
import { createRequestOption } from '../../shared';
import { dateToNgb } from '../../shared/model/format-utils';

export type EntityResponseType = HttpResponse<TeamInvitation>;

@Injectable()
export class TeamInvitationService {

    private resourceUrl =  SERVER_API_URL + 'api/team-invitations';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(teamInvitation: TeamInvitation): Observable<EntityResponseType> {
        const copy = this.convert(teamInvitation);
        return this.http.post<TeamInvitation>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    accept(teamInvitation: TeamInvitation): Observable<EntityResponseType> {
        const copy = this.convert(teamInvitation);
        return this.http.post<TeamInvitation>(`${this.resourceUrl}/accept`, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    decline(teamInvitation: TeamInvitation): Observable<EntityResponseType> {
        const copy = this.convert(teamInvitation);
        return this.http.post<TeamInvitation>(`${this.resourceUrl}/decline`, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }
    update(teamInvitation: TeamInvitation): Observable<EntityResponseType> {
        const copy = this.convert(teamInvitation);
        return this.http.put<TeamInvitation>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<TeamInvitation>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<TeamInvitation[]>> {
        const options = createRequestOption(req);
        return this.http.get<TeamInvitation[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<TeamInvitation[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: TeamInvitation = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<TeamInvitation[]>): HttpResponse<TeamInvitation[]> {
        const jsonResponse: TeamInvitation[] = res.body;
        const body: TeamInvitation[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to TeamInvitation.
     */
    private convertItemFromServer(teamInvitation: TeamInvitation): TeamInvitation {
        const copy: TeamInvitation = Object.assign({}, teamInvitation);
        copy.date = this.dateUtils
            .convertLocalDateFromServer(teamInvitation.date);
        return copy;
    }

    /**
     * Convert a TeamInvitation to a JSON which can be sent to the server.
     */
    private convert(teamInvitation: TeamInvitation): TeamInvitation {
        const copy: TeamInvitation = Object.assign({}, teamInvitation);
        copy.date = this.dateUtils
            .convertLocalDateToServer(dateToNgb(teamInvitation.date));
        return copy;
    }
}
