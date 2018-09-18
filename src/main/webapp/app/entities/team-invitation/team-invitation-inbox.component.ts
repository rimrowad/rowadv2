import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { TeamInvitation } from './team-invitation.model';
import { TeamInvitationService } from './team-invitation.service';
import { Principal } from '../../shared';

@Component({
    selector: 'jhi-team-invitation-index',
    templateUrl: './team-invitation-inbox.component.html'
})
export class TeamInvitationInboxComponent implements OnInit, OnDestroy {

    currentAccount: any;
    teamInvitations: TeamInvitation[];
    eventSubscriber: Subscription;

    constructor(
        private teamInvitationService: TeamInvitationService,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private eventManager: JhiEventManager
    ) {
    }

    loadAll() {
        this.teamInvitationService.query({
            'receiverLogin': this.currentAccount.login}).subscribe(
                (res: HttpResponse<TeamInvitation[]>) => {
                    this.teamInvitations = res.body;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
        );
    }
    accept(invitation: TeamInvitation) {
        this.teamInvitationService.accept(invitation).subscribe(
            () => this.eventManager.broadcast({ name: 'teamInvitationListModification', content: 'OK'}),
            (res: HttpErrorResponse) => this.onError(res.message));
    }
    decline(invitation: TeamInvitation) {
        this.teamInvitationService.decline(invitation).subscribe(
            () => this.eventManager.broadcast({ name: 'teamInvitationListModification', content: 'OK'}),
            (res: HttpErrorResponse) => this.onError(res.message));
    }
    ngOnInit() {
        this.principal.identity().then((account) => {
            this.currentAccount = account;
            this.loadAll();
        });
        this.registerChangeInTeamInvitations();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: TeamInvitation) {
        return item.id;
    }
    registerChangeInTeamInvitations() {
        this.eventSubscriber = this.eventManager.subscribe('teamInvitationListModification', (response) => this.loadAll());
    }
    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
