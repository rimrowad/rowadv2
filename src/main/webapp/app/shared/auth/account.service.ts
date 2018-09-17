import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';
import { Investor } from '../../entities/investor';
import { TeamMember } from '../../entities/team-member';

@Injectable()
export class AccountService {
    constructor(private http: HttpClient) { }
    get(): Observable<HttpResponse<Account>> {
        return this.http.get<Account>(SERVER_API_URL + 'api/account', { observe: 'response' });
    }
    getInvestor(): Observable<HttpResponse<Investor>> {
        return this.http.get<Investor>(SERVER_API_URL + 'api/account/investor', { observe: 'response' });
    }
    getMember(): Observable<HttpResponse<TeamMember>> {
        return this.http.get<TeamMember>(SERVER_API_URL + 'api/account/member', { observe: 'response' });
    }
    save(account: any): Observable<HttpResponse<any>> {
        return this.http.post(SERVER_API_URL + 'api/account', account, { observe: 'response' });
    }
    saveInvestor(account: any): Observable<HttpResponse<any>> {
        return this.http.post(SERVER_API_URL + 'api/account/investor', account, { observe: 'response' });
    }
    saveMember(account: any): Observable<HttpResponse<any>> {
        return this.http.post(SERVER_API_URL + 'api/account/member', account, { observe: 'response' });
    }
}
