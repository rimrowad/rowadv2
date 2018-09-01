import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { TeamMember } from './team-member.model';
import { TeamMemberService } from './team-member.service';

@Injectable()
export class TeamMemberPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private teamMemberService: TeamMemberService

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
                this.teamMemberService.find(id)
                    .subscribe((teamMemberResponse: HttpResponse<TeamMember>) => {
                        const teamMember: TeamMember = teamMemberResponse.body;
                        if (teamMember.dateOfBirth) {
                            teamMember.dateOfBirth = {
                                year: teamMember.dateOfBirth.getFullYear(),
                                month: teamMember.dateOfBirth.getMonth() + 1,
                                day: teamMember.dateOfBirth.getDate()
                            };
                        }
                        this.ngbModalRef = this.teamMemberModalRef(component, teamMember);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.teamMemberModalRef(component, new TeamMember());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    teamMemberModalRef(component: Component, teamMember: TeamMember): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.teamMember = teamMember;
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
