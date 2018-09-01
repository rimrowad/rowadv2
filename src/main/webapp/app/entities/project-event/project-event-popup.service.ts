import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { ProjectEvent } from './project-event.model';
import { ProjectEventService } from './project-event.service';

@Injectable()
export class ProjectEventPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private projectEventService: ProjectEventService

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
                this.projectEventService.find(id)
                    .subscribe((projectEventResponse: HttpResponse<ProjectEvent>) => {
                        const projectEvent: ProjectEvent = projectEventResponse.body;
                        if (projectEvent.creationDate) {
                            projectEvent.creationDate = {
                                year: projectEvent.creationDate.getFullYear(),
                                month: projectEvent.creationDate.getMonth() + 1,
                                day: projectEvent.creationDate.getDate()
                            };
                        }
                        this.ngbModalRef = this.projectEventModalRef(component, projectEvent);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.projectEventModalRef(component, new ProjectEvent());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    projectEventModalRef(component: Component, projectEvent: ProjectEvent): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.projectEvent = projectEvent;
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
