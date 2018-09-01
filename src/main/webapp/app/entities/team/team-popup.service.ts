import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { Team } from './team.model';
import { TeamService } from './team.service';

@Injectable()
export class TeamPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private teamService: TeamService

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
                this.teamService.find(id)
                    .subscribe((teamResponse: HttpResponse<Team>) => {
                        const team: Team = teamResponse.body;
                        if (team.creationDate) {
                            team.creationDate = {
                                year: team.creationDate.getFullYear(),
                                month: team.creationDate.getMonth() + 1,
                                day: team.creationDate.getDate()
                            };
                        }
                        this.ngbModalRef = this.teamModalRef(component, team);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.teamModalRef(component, new Team());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    teamModalRef(component: Component, team: Team): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.team = team;
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
