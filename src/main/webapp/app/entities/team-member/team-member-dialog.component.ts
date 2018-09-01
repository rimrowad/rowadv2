import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { TeamMember } from './team-member.model';
import { TeamMemberPopupService } from './team-member-popup.service';
import { TeamMemberService } from './team-member.service';
import { Team, TeamService } from '../team';
import { User, UserService } from '../../shared';

@Component({
    selector: 'jhi-team-member-dialog',
    templateUrl: './team-member-dialog.component.html'
})
export class TeamMemberDialogComponent implements OnInit {

    teamMember: TeamMember;
    isSaving: boolean;

    teams: Team[];

    users: User[];
    dateOfBirthDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private teamMemberService: TeamMemberService,
        private teamService: TeamService,
        private userService: UserService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.teamService.query()
            .subscribe((res: HttpResponse<Team[]>) => { this.teams = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.userService.query()
            .subscribe((res: HttpResponse<User[]>) => { this.users = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.teamMember.id !== undefined) {
            this.subscribeToSaveResponse(
                this.teamMemberService.update(this.teamMember));
        } else {
            this.subscribeToSaveResponse(
                this.teamMemberService.create(this.teamMember));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<TeamMember>>) {
        result.subscribe((res: HttpResponse<TeamMember>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: TeamMember) {
        this.eventManager.broadcast({ name: 'teamMemberListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackTeamById(index: number, item: Team) {
        return item.id;
    }

    trackUserById(index: number, item: User) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-team-member-popup',
    template: ''
})
export class TeamMemberPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private teamMemberPopupService: TeamMemberPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.teamMemberPopupService
                    .open(TeamMemberDialogComponent as Component, params['id']);
            } else {
                this.teamMemberPopupService
                    .open(TeamMemberDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
