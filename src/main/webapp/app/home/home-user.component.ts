import { Component, OnInit } from '@angular/core';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Account, LoginModalService, Principal } from '../shared';
import { RegisterType } from '../account';
import { Router } from '@angular/router';
import { TeamMemberService, TeamMember } from '../entities/team-member';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

@Component({
    selector: 'jhi-home-user',
    templateUrl: './home-user.component.html',
    styleUrls: [
        'home.css'
    ]

})
export class HomeUserComponent implements OnInit {
    account: Account;
    modalRef: NgbModalRef;
    member: TeamMember;

    constructor(
        private principal: Principal,
        private loginModalService: LoginModalService,
        private registerType: RegisterType,
        private router: Router,
        private memberService: TeamMemberService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.principal.identity().then((account) => {
            this.account = account;
            this.memberService.query({ 'userLogin.equals': account.login })
                .subscribe(
                    (res: HttpResponse<TeamMember[]>) => this.member = res.body[0],
                    () => this.member = {}
                );
        });
        this.registerEvents();
    }

    hasTeam(): boolean {
        if (!this.member) {
            return false;
        }
        return this.member.team !== null;
    }

    registerEvents() {
        this.eventManager.subscribe('authenticationSuccess', (message) => {
            this.principal.identity().then((account) => {
                this.account = account;
            });
        });
        let list = ['teamListModification', 'teamMemberListModification'];
        for(let item in list) {
            this.eventManager.subscribe(item, (message) => {
                this.principal.identity(true).then((account) => {
                    this.account = account;
                });
            });
        }
        
    }

    quitTeam() {
        this.member.team = null;
        this.subscribeToSaveResponse(
            this.memberService.update(this.member)
        );
    }
    private subscribeToSaveResponse(result: Observable<HttpResponse<TeamMember>>) {
        result.subscribe((res: HttpResponse<TeamMember>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: TeamMember) {
        this.eventManager.broadcast({ name: 'teamMemberListModification', content: 'OK'});
    }

    private onSaveError() {
    }
    isAuthenticated() {
        return this.principal.isAuthenticated();
    }
}
