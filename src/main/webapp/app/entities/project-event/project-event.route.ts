import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { ProjectEventComponent } from './project-event.component';
import { ProjectEventDetailComponent } from './project-event-detail.component';
import { ProjectEventPopupComponent } from './project-event-dialog.component';
import { ProjectEventDeletePopupComponent } from './project-event-delete-dialog.component';

@Injectable()
export class ProjectEventResolvePagingParams implements Resolve<any> {

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

export const projectEventRoute: Routes = [
    {
        path: 'project-event',
        component: ProjectEventComponent,
        resolve: {
            'pagingParams': ProjectEventResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'rowadApp.projectEvent.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'project-event/:id',
        component: ProjectEventDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'rowadApp.projectEvent.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const projectEventPopupRoute: Routes = [
    {
        path: 'project-event-new',
        component: ProjectEventPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'rowadApp.projectEvent.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'project-event/:id/edit',
        component: ProjectEventPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'rowadApp.projectEvent.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'project-event/:id/delete',
        component: ProjectEventDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'rowadApp.projectEvent.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
