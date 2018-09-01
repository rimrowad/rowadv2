import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Parameter } from './parameter.model';
import { ParameterPopupService } from './parameter-popup.service';
import { ParameterService } from './parameter.service';

@Component({
    selector: 'jhi-parameter-dialog',
    templateUrl: './parameter-dialog.component.html'
})
export class ParameterDialogComponent implements OnInit {

    parameter: Parameter;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private parameterService: ParameterService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.parameter.id !== undefined) {
            this.subscribeToSaveResponse(
                this.parameterService.update(this.parameter));
        } else {
            this.subscribeToSaveResponse(
                this.parameterService.create(this.parameter));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Parameter>>) {
        result.subscribe((res: HttpResponse<Parameter>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Parameter) {
        this.eventManager.broadcast({ name: 'parameterListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-parameter-popup',
    template: ''
})
export class ParameterPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private parameterPopupService: ParameterPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.parameterPopupService
                    .open(ParameterDialogComponent as Component, params['id']);
            } else {
                this.parameterPopupService
                    .open(ParameterDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
