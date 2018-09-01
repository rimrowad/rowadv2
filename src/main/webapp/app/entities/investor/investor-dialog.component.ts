import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Investor } from './investor.model';
import { InvestorPopupService } from './investor-popup.service';
import { InvestorService } from './investor.service';
import { User, UserService } from '../../shared';

@Component({
    selector: 'jhi-investor-dialog',
    templateUrl: './investor-dialog.component.html'
})
export class InvestorDialogComponent implements OnInit {

    investor: Investor;
    isSaving: boolean;

    users: User[];
    dateOfBirthDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private investorService: InvestorService,
        private userService: UserService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.userService.query()
            .subscribe((res: HttpResponse<User[]>) => { this.users = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.investor.id !== undefined) {
            this.subscribeToSaveResponse(
                this.investorService.update(this.investor));
        } else {
            this.subscribeToSaveResponse(
                this.investorService.create(this.investor));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Investor>>) {
        result.subscribe((res: HttpResponse<Investor>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Investor) {
        this.eventManager.broadcast({ name: 'investorListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackUserById(index: number, item: User) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-investor-popup',
    template: ''
})
export class InvestorPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private investorPopupService: InvestorPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.investorPopupService
                    .open(InvestorDialogComponent as Component, params['id']);
            } else {
                this.investorPopupService
                    .open(InvestorDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
