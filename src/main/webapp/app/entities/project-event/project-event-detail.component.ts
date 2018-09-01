import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { ProjectEvent } from './project-event.model';
import { ProjectEventService } from './project-event.service';

@Component({
    selector: 'jhi-project-event-detail',
    templateUrl: './project-event-detail.component.html'
})
export class ProjectEventDetailComponent implements OnInit, OnDestroy {

    projectEvent: ProjectEvent;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private projectEventService: ProjectEventService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInProjectEvents();
    }

    load(id) {
        this.projectEventService.find(id)
            .subscribe((projectEventResponse: HttpResponse<ProjectEvent>) => {
                this.projectEvent = projectEventResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInProjectEvents() {
        this.eventSubscriber = this.eventManager.subscribe(
            'projectEventListModification',
            (response) => this.load(this.projectEvent.id)
        );
    }
}
