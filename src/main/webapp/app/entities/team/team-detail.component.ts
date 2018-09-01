import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Team } from './team.model';
import { TeamService } from './team.service';

@Component({
    selector: 'jhi-team-detail',
    templateUrl: './team-detail.component.html'
})
export class TeamDetailComponent implements OnInit, OnDestroy {

    team: Team;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private teamService: TeamService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInTeams();
    }

    load(id) {
        this.teamService.find(id)
            .subscribe((teamResponse: HttpResponse<Team>) => {
                this.team = teamResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInTeams() {
        this.eventSubscriber = this.eventManager.subscribe(
            'teamListModification',
            (response) => this.load(this.team.id)
        );
    }
}
