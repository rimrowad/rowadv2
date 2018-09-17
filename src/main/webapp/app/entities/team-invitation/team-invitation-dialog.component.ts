import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { TeamInvitation } from './team-invitation.model';
import { TeamInvitationPopupService } from './team-invitation-popup.service';
import { TeamInvitationService } from './team-invitation.service';
import { TeamMember, TeamMemberService } from '../team-member';
import { Team, TeamService } from '../team';

@Component({
    selector: 'jhi-team-invitation-dialog',
    templateUrl: './team-invitation-dialog.component.html'
})
export class TeamInvitationDialogComponent implements OnInit {

    teamInvitation: TeamInvitation;
    isSaving: boolean;

    teammembers: TeamMember[];

    teams: Team[];
    dateDp: any;
    receiverError: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private teamInvitationService: TeamInvitationService,
        private teamMemberService: TeamMemberService,
        private teamService: TeamService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.teamMemberService.query()
            .subscribe((res: HttpResponse<TeamMember[]>) => { this.teammembers = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.teamService.query()
            .subscribe((res: HttpResponse<Team[]>) => { this.teams = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    findReceiver(login: string) {
        this.teamMemberService.query({ 'userLogin.equals': login }).subscribe(
            (res: HttpResponse<TeamMember[]>) => {
                if (res.body.length > 0) {
                    this.teamInvitation.receiver = res.body.pop();
                    this.receiverError = false;
                } else {
                    this.receiverError = true;
                    this.teamInvitation.receiver = {};
                    this.teamInvitation.receiver.user = {};
                }
            }
        );
        if (login === '') {
            this.receiverError = false;
        }
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.teamInvitation.id !== undefined) {
            this.subscribeToSaveResponse(
                this.teamInvitationService.update(this.teamInvitation));
        } else {
            this.subscribeToSaveResponse(
                this.teamInvitationService.create(this.teamInvitation));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<TeamInvitation>>) {
        result.subscribe((res: HttpResponse<TeamInvitation>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: TeamInvitation) {
        this.eventManager.broadcast({ name: 'teamInvitationListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackTeamMemberById(index: number, item: TeamMember) {
        return item.id;
    }

    trackTeamById(index: number, item: Team) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-team-invitation-popup',
    template: ''
})
export class TeamInvitationPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private teamInvitationPopupService: TeamInvitationPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.teamInvitationPopupService
                    .open(TeamInvitationDialogComponent as Component, params['id']);
            } else {
                this.teamInvitationPopupService
                    .open(TeamInvitationDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
