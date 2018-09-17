import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { TeamInvitation } from './team-invitation.model';
import { TeamInvitationService } from './team-invitation.service';
import { ITEMS_PER_PAGE, Principal } from '../../shared';
import { dateToNgb } from '../../shared/model/format-utils';

@Component({
    selector: 'jhi-team-invitation-index',
    templateUrl: './team-invitation-inbox.component.html'
})
export class TeamInvitationInboxComponent implements OnInit, OnDestroy {

currentAccount: any;
    teamInvitations: TeamInvitation[];
    error: any;
    success: any;
    eventSubscriber: Subscription;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;

    constructor(
        private teamInvitationService: TeamInvitationService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe((data) => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
        });
    }

    loadAll() {
        this.teamInvitationService.query({
            'receiverLogin': this.currentAccount.login,
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort()}).subscribe(
                (res: HttpResponse<TeamInvitation[]>) => this.onSuccess(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
        );
    }
    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }
    transition() {
        this.router.navigate(['/team-invitation'], {queryParams:
            {
                page: this.page,
                size: this.itemsPerPage,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll();
    }
    accept(invitation: TeamInvitation) {
        this.teamInvitationService.accept(invitation).subscribe(
            () => this.eventManager.broadcast({ name: 'teamInvitationListModification', content: 'OK'}),
            (res: HttpErrorResponse) => this.onError(res.message));
    }
    decline(invitation: TeamInvitation) {
        this.teamInvitationService.decline(invitation).subscribe(
            () => this.eventManager.broadcast({ name: 'teamInvitationListModification', content: 'OK'}),
            (res: HttpErrorResponse) => this.onError(res.message));
    }
    clear() {
        this.page = 0;
        this.router.navigate(['/team-invitation', {
            page: this.page,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        }]);
        this.loadAll();
    }
    ngOnInit() {
        this.principal.identity().then((account) => {
            this.currentAccount = account;
            this.loadAll();
        });
        this.registerChangeInTeamInvitations();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: TeamInvitation) {
        return item.id;
    }
    registerChangeInTeamInvitations() {
        this.eventSubscriber = this.eventManager.subscribe('teamInvitationListModification', (response) => this.loadAll());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private onSuccess(data, headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        this.queryCount = this.totalItems;
        // this.page = pagingParams.page;
        this.teamInvitations = data;
    }
    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
