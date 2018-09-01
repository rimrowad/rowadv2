import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Investor } from './investor.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Investor>;

@Injectable()
export class InvestorService {

    private resourceUrl =  SERVER_API_URL + 'api/investors';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(investor: Investor): Observable<EntityResponseType> {
        const copy = this.convert(investor);
        return this.http.post<Investor>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(investor: Investor): Observable<EntityResponseType> {
        const copy = this.convert(investor);
        return this.http.put<Investor>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Investor>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Investor[]>> {
        const options = createRequestOption(req);
        return this.http.get<Investor[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Investor[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Investor = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Investor[]>): HttpResponse<Investor[]> {
        const jsonResponse: Investor[] = res.body;
        const body: Investor[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Investor.
     */
    private convertItemFromServer(investor: Investor): Investor {
        const copy: Investor = Object.assign({}, investor);
        copy.dateOfBirth = this.dateUtils
            .convertLocalDateFromServer(investor.dateOfBirth);
        return copy;
    }

    /**
     * Convert a Investor to a JSON which can be sent to the server.
     */
    private convert(investor: Investor): Investor {
        const copy: Investor = Object.assign({}, investor);
        copy.dateOfBirth = this.dateUtils
            .convertLocalDateToServer(investor.dateOfBirth);
        return copy;
    }
}
