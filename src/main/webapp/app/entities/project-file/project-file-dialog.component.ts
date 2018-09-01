import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { ProjectFile } from './project-file.model';
import { ProjectFilePopupService } from './project-file-popup.service';
import { ProjectFileService } from './project-file.service';
import { Project, ProjectService } from '../project';

@Component({
    selector: 'jhi-project-file-dialog',
    templateUrl: './project-file-dialog.component.html'
})
export class ProjectFileDialogComponent implements OnInit {

    projectFile: ProjectFile;
    isSaving: boolean;

    projects: Project[];

    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private projectFileService: ProjectFileService,
        private projectService: ProjectService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.projectService.query()
            .subscribe((res: HttpResponse<Project[]>) => { this.projects = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.projectFile.id !== undefined) {
            this.subscribeToSaveResponse(
                this.projectFileService.update(this.projectFile));
        } else {
            this.subscribeToSaveResponse(
                this.projectFileService.create(this.projectFile));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ProjectFile>>) {
        result.subscribe((res: HttpResponse<ProjectFile>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: ProjectFile) {
        this.eventManager.broadcast({ name: 'projectFileListModification', content: 'OK'});
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
}

@Component({
    selector: 'jhi-project-file-popup',
    template: ''
})
export class ProjectFilePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private projectFilePopupService: ProjectFilePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.projectFilePopupService
                    .open(ProjectFileDialogComponent as Component, params['id']);
            } else {
                this.projectFilePopupService
                    .open(ProjectFileDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
