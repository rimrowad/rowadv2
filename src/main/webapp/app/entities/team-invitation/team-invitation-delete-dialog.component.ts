import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { TeamInvitation } from './team-invitation.model';
import { TeamInvitationPopupService } from './team-invitation-popup.service';
import { TeamInvitationService } from './team-invitation.service';

@Component({
    selector: 'jhi-team-invitation-delete-dialog',
    templateUrl: './team-invitation-delete-dialog.component.html'
})
export class TeamInvitationDeleteDialogComponent {

    teamInvitation: TeamInvitation;

    constructor(
        private teamInvitationService: TeamInvitationService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.teamInvitationService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'teamInvitationListModification',
                content: 'Deleted an teamInvitation'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-team-invitation-delete-popup',
    template: ''
})
export class TeamInvitationDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private teamInvitationPopupService: TeamInvitationPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.teamInvitationPopupService
                .open(TeamInvitationDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
