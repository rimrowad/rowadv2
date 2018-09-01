import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ProjectFile } from './project-file.model';
import { ProjectFilePopupService } from './project-file-popup.service';
import { ProjectFileService } from './project-file.service';

@Component({
    selector: 'jhi-project-file-delete-dialog',
    templateUrl: './project-file-delete-dialog.component.html'
})
export class ProjectFileDeleteDialogComponent {

    projectFile: ProjectFile;

    constructor(
        private projectFileService: ProjectFileService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.projectFileService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'projectFileListModification',
                content: 'Deleted an projectFile'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-project-file-delete-popup',
    template: ''
})
export class ProjectFileDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private projectFilePopupService: ProjectFilePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.projectFilePopupService
                .open(ProjectFileDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
