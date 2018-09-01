import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Parameter } from './parameter.model';
import { ParameterPopupService } from './parameter-popup.service';
import { ParameterService } from './parameter.service';

@Component({
    selector: 'jhi-parameter-delete-dialog',
    templateUrl: './parameter-delete-dialog.component.html'
})
export class ParameterDeleteDialogComponent {

    parameter: Parameter;

    constructor(
        private parameterService: ParameterService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.parameterService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'parameterListModification',
                content: 'Deleted an parameter'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-parameter-delete-popup',
    template: ''
})
export class ParameterDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private parameterPopupService: ParameterPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.parameterPopupService
                .open(ParameterDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
