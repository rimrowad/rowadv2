import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Parameter } from './parameter.model';
import { ParameterService } from './parameter.service';

@Component({
    selector: 'jhi-parameter-detail',
    templateUrl: './parameter-detail.component.html'
})
export class ParameterDetailComponent implements OnInit, OnDestroy {

    parameter: Parameter;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private parameterService: ParameterService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInParameters();
    }

    load(id) {
        this.parameterService.find(id)
            .subscribe((parameterResponse: HttpResponse<Parameter>) => {
                this.parameter = parameterResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInParameters() {
        this.eventSubscriber = this.eventManager.subscribe(
            'parameterListModification',
            (response) => this.load(this.parameter.id)
        );
    }
}
