import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { TeamInvitation } from './team-invitation.model';
import { TeamInvitationService } from './team-invitation.service';

@Injectable()
export class TeamInvitationPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private teamInvitationService: TeamInvitationService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.teamInvitationService.find(id)
                    .subscribe((teamInvitationResponse: HttpResponse<TeamInvitation>) => {
                        const teamInvitation: TeamInvitation = teamInvitationResponse.body;
                        if (teamInvitation.date) {
                            teamInvitation.date = {
                                year: teamInvitation.date.getFullYear(),
                                month: teamInvitation.date.getMonth() + 1,
                                day: teamInvitation.date.getDate()
                            };
                        }
                        this.ngbModalRef = this.teamInvitationModalRef(component, teamInvitation);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    const invitation: TeamInvitation = new TeamInvitation();
                    invitation.receiver = {};
                    invitation.receiver.user = {};
                    this.ngbModalRef = this.teamInvitationModalRef(component, invitation);
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    teamInvitationModalRef(component: Component, teamInvitation: TeamInvitation): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.teamInvitation = teamInvitation;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
