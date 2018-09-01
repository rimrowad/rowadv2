import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { TeamMember } from './team-member.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<TeamMember>;

@Injectable()
export class TeamMemberService {

    private resourceUrl =  SERVER_API_URL + 'api/team-members';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(teamMember: TeamMember): Observable<EntityResponseType> {
        const copy = this.convert(teamMember);
        return this.http.post<TeamMember>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(teamMember: TeamMember): Observable<EntityResponseType> {
        const copy = this.convert(teamMember);
        return this.http.put<TeamMember>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<TeamMember>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<TeamMember[]>> {
        const options = createRequestOption(req);
        return this.http.get<TeamMember[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<TeamMember[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: TeamMember = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<TeamMember[]>): HttpResponse<TeamMember[]> {
        const jsonResponse: TeamMember[] = res.body;
        const body: TeamMember[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to TeamMember.
     */
    private convertItemFromServer(teamMember: TeamMember): TeamMember {
        const copy: TeamMember = Object.assign({}, teamMember);
        copy.dateOfBirth = this.dateUtils
            .convertLocalDateFromServer(teamMember.dateOfBirth);
        return copy;
    }

    /**
     * Convert a TeamMember to a JSON which can be sent to the server.
     */
    private convert(teamMember: TeamMember): TeamMember {
        const copy: TeamMember = Object.assign({}, teamMember);
        copy.dateOfBirth = this.dateUtils
            .convertLocalDateToServer(teamMember.dateOfBirth);
        return copy;
    }
}
