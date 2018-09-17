import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { TeamInvitationComponent } from './team-invitation.component';
import { TeamInvitationDetailComponent } from './team-invitation-detail.component';
import { TeamInvitationPopupComponent } from './team-invitation-dialog.component';
import { TeamInvitationDeletePopupComponent } from './team-invitation-delete-dialog.component';
import { TeamInvitationInboxComponent } from './team-invitation-inbox.component';

@Injectable()
export class TeamInvitationResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const teamInvitationRoute: Routes = [
    {
        path: 'team-invitation',
        component: TeamInvitationComponent,
        resolve: {
            'pagingParams': TeamInvitationResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'rowadApp.teamInvitation.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'team-invitation-inbox',
        component: TeamInvitationInboxComponent,
        resolve: {
            'pagingParams': TeamInvitationResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'rowadApp.teamInvitation.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'team-invitation/:id',
        component: TeamInvitationDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'rowadApp.teamInvitation.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const teamInvitationPopupRoute: Routes = [
    {
        path: 'team-invitation-new',
        component: TeamInvitationPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'rowadApp.teamInvitation.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'team-invitation/:id/edit',
        component: TeamInvitationPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'rowadApp.teamInvitation.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'team-invitation/:id/delete',
        component: TeamInvitationDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'rowadApp.teamInvitation.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
