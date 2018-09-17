import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { TeamInvitation } from './team-invitation.model';
import { TeamInvitationService } from './team-invitation.service';

@Component({
    selector: 'jhi-team-invitation-detail',
    templateUrl: './team-invitation-detail.component.html'
})
export class TeamInvitationDetailComponent implements OnInit, OnDestroy {

    teamInvitation: TeamInvitation;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private teamInvitationService: TeamInvitationService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInTeamInvitations();
    }

    load(id) {
        this.teamInvitationService.find(id)
            .subscribe((teamInvitationResponse: HttpResponse<TeamInvitation>) => {
                this.teamInvitation = teamInvitationResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInTeamInvitations() {
        this.eventSubscriber = this.eventManager.subscribe(
            'teamInvitationListModification',
            (response) => this.load(this.teamInvitation.id)
        );
    }
}
