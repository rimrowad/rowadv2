import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ProjectEvent } from './project-event.model';
import { ProjectEventPopupService } from './project-event-popup.service';
import { ProjectEventService } from './project-event.service';

@Component({
    selector: 'jhi-project-event-delete-dialog',
    templateUrl: './project-event-delete-dialog.component.html'
})
export class ProjectEventDeleteDialogComponent {

    projectEvent: ProjectEvent;

    constructor(
        private projectEventService: ProjectEventService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.projectEventService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'projectEventListModification',
                content: 'Deleted an projectEvent'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-project-event-delete-popup',
    template: ''
})
export class ProjectEventDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private projectEventPopupService: ProjectEventPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.projectEventPopupService
                .open(ProjectEventDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
