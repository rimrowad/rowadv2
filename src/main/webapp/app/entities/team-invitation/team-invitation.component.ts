import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { TeamInvitation } from './team-invitation.model';
import { TeamInvitationService } from './team-invitation.service';
import { Principal } from '../../shared';

@Component({
    selector: 'jhi-team-invitation',
    templateUrl: './team-invitation.component.html'
})
export class TeamInvitationComponent implements OnInit, OnDestroy {
    teamInvitations: TeamInvitation[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private teamInvitationService: TeamInvitationService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.teamInvitationService.query({'senderLogin': this.currentAccount.login})
            .subscribe(
            (res: HttpResponse<TeamInvitation[]>) => {
                this.teamInvitations = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
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
