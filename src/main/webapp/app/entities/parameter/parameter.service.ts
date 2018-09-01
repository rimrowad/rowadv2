import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Parameter } from './parameter.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Parameter>;

@Injectable()
export class ParameterService {

    private resourceUrl =  SERVER_API_URL + 'api/parameters';

    constructor(private http: HttpClient) { }

    create(parameter: Parameter): Observable<EntityResponseType> {
        const copy = this.convert(parameter);
        return this.http.post<Parameter>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(parameter: Parameter): Observable<EntityResponseType> {
        const copy = this.convert(parameter);
        return this.http.put<Parameter>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Parameter>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Parameter[]>> {
        const options = createRequestOption(req);
        return this.http.get<Parameter[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Parameter[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Parameter = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Parameter[]>): HttpResponse<Parameter[]> {
        const jsonResponse: Parameter[] = res.body;
        const body: Parameter[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Parameter.
     */
    private convertItemFromServer(parameter: Parameter): Parameter {
        const copy: Parameter = Object.assign({}, parameter);
        return copy;
    }

    /**
     * Convert a Parameter to a JSON which can be sent to the server.
     */
    private convert(parameter: Parameter): Parameter {
        const copy: Parameter = Object.assign({}, parameter);
        return copy;
    }
}
