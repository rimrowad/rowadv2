import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { ProjectEvent } from './project-event.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<ProjectEvent>;

@Injectable()
export class ProjectEventService {

    private resourceUrl =  SERVER_API_URL + 'api/project-events';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(projectEvent: ProjectEvent): Observable<EntityResponseType> {
        const copy = this.convert(projectEvent);
        return this.http.post<ProjectEvent>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(projectEvent: ProjectEvent): Observable<EntityResponseType> {
        const copy = this.convert(projectEvent);
        return this.http.put<ProjectEvent>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ProjectEvent>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<ProjectEvent[]>> {
        const options = createRequestOption(req);
        return this.http.get<ProjectEvent[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<ProjectEvent[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: ProjectEvent = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<ProjectEvent[]>): HttpResponse<ProjectEvent[]> {
        const jsonResponse: ProjectEvent[] = res.body;
        const body: ProjectEvent[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to ProjectEvent.
     */
    private convertItemFromServer(projectEvent: ProjectEvent): ProjectEvent {
        const copy: ProjectEvent = Object.assign({}, projectEvent);
        copy.creationDate = this.dateUtils
            .convertLocalDateFromServer(projectEvent.creationDate);
        return copy;
    }

    /**
     * Convert a ProjectEvent to a JSON which can be sent to the server.
     */
    private convert(projectEvent: ProjectEvent): ProjectEvent {
        const copy: ProjectEvent = Object.assign({}, projectEvent);
        copy.creationDate = this.dateUtils
            .convertLocalDateToServer(projectEvent.creationDate);
        return copy;
    }
}
