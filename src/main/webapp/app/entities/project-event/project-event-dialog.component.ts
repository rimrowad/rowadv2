import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ProjectEvent } from './project-event.model';
import { ProjectEventPopupService } from './project-event-popup.service';
import { ProjectEventService } from './project-event.service';
import { Project, ProjectService } from '../project';
import { User, UserService } from '../../shared';

@Component({
    selector: 'jhi-project-event-dialog',
    templateUrl: './project-event-dialog.component.html'
})
export class ProjectEventDialogComponent implements OnInit {

    projectEvent: ProjectEvent;
    isSaving: boolean;

    projects: Project[];

    users: User[];
    creationDateDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private projectEventService: ProjectEventService,
        private projectService: ProjectService,
        private userService: UserService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.projectService.query()
            .subscribe((res: HttpResponse<Project[]>) => { this.projects = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.userService.query()
            .subscribe((res: HttpResponse<User[]>) => { this.users = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.projectEvent.id !== undefined) {
            this.subscribeToSaveResponse(
                this.projectEventService.update(this.projectEvent));
        } else {
            this.subscribeToSaveResponse(
                this.projectEventService.create(this.projectEvent));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ProjectEvent>>) {
        result.subscribe((res: HttpResponse<ProjectEvent>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: ProjectEvent) {
        this.eventManager.broadcast({ name: 'projectEventListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackProjectById(index: number, item: Project) {
        return item.id;
    }

    trackUserById(index: number, item: User) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-project-event-popup',
    template: ''
})
export class ProjectEventPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private projectEventPopupService: ProjectEventPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.projectEventPopupService
                    .open(ProjectEventDialogComponent as Component, params['id']);
            } else {
                this.projectEventPopupService
                    .open(ProjectEventDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
