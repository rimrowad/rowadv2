import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';

import { ProjectFile } from './project-file.model';
import { ProjectFileService } from './project-file.service';

@Component({
    selector: 'jhi-project-file-detail',
    templateUrl: './project-file-detail.component.html'
})
export class ProjectFileDetailComponent implements OnInit, OnDestroy {

    projectFile: ProjectFile;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private dataUtils: JhiDataUtils,
        private projectFileService: ProjectFileService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInProjectFiles();
    }

    load(id) {
        this.projectFileService.find(id)
            .subscribe((projectFileResponse: HttpResponse<ProjectFile>) => {
                this.projectFile = projectFileResponse.body;
            });
    }
    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInProjectFiles() {
        this.eventSubscriber = this.eventManager.subscribe(
            'projectFileListModification',
            (response) => this.load(this.projectFile.id)
        );
    }
}
