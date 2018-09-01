import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { ProjectFile } from './project-file.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<ProjectFile>;

@Injectable()
export class ProjectFileService {

    private resourceUrl =  SERVER_API_URL + 'api/project-files';

    constructor(private http: HttpClient) { }

    create(projectFile: ProjectFile): Observable<EntityResponseType> {
        const copy = this.convert(projectFile);
        return this.http.post<ProjectFile>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(projectFile: ProjectFile): Observable<EntityResponseType> {
        const copy = this.convert(projectFile);
        return this.http.put<ProjectFile>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ProjectFile>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<ProjectFile[]>> {
        const options = createRequestOption(req);
        return this.http.get<ProjectFile[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<ProjectFile[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: ProjectFile = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<ProjectFile[]>): HttpResponse<ProjectFile[]> {
        const jsonResponse: ProjectFile[] = res.body;
        const body: ProjectFile[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to ProjectFile.
     */
    private convertItemFromServer(projectFile: ProjectFile): ProjectFile {
        const copy: ProjectFile = Object.assign({}, projectFile);
        return copy;
    }

    /**
     * Convert a ProjectFile to a JSON which can be sent to the server.
     */
    private convert(projectFile: ProjectFile): ProjectFile {
        const copy: ProjectFile = Object.assign({}, projectFile);
        return copy;
    }
}
