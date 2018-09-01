import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { TeamMemberComponent } from './team-member.component';
import { TeamMemberDetailComponent } from './team-member-detail.component';
import { TeamMemberPopupComponent } from './team-member-dialog.component';
import { TeamMemberDeletePopupComponent } from './team-member-delete-dialog.component';

@Injectable()
export class TeamMemberResolvePagingParams implements Resolve<any> {

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

export const teamMemberRoute: Routes = [
    {
        path: 'team-member',
        component: TeamMemberComponent,
        resolve: {
            'pagingParams': TeamMemberResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'rowadApp.teamMember.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'team-member/:id',
        component: TeamMemberDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'rowadApp.teamMember.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const teamMemberPopupRoute: Routes = [
    {
        path: 'team-member-new',
        component: TeamMemberPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'rowadApp.teamMember.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'team-member/:id/edit',
        component: TeamMemberPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'rowadApp.teamMember.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'team-member/:id/delete',
        component: TeamMemberDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'rowadApp.teamMember.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
