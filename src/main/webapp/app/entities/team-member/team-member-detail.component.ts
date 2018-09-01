import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { TeamMember } from './team-member.model';
import { TeamMemberService } from './team-member.service';

@Component({
    selector: 'jhi-team-member-detail',
    templateUrl: './team-member-detail.component.html'
})
export class TeamMemberDetailComponent implements OnInit, OnDestroy {

    teamMember: TeamMember;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private teamMemberService: TeamMemberService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInTeamMembers();
    }

    load(id) {
        this.teamMemberService.find(id)
            .subscribe((teamMemberResponse: HttpResponse<TeamMember>) => {
                this.teamMember = teamMemberResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInTeamMembers() {
        this.eventSubscriber = this.eventManager.subscribe(
            'teamMemberListModification',
            (response) => this.load(this.teamMember.id)
        );
    }
}
