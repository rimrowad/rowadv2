import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { ProjectFileComponent } from './project-file.component';
import { ProjectFileDetailComponent } from './project-file-detail.component';
import { ProjectFilePopupComponent } from './project-file-dialog.component';
import { ProjectFileDeletePopupComponent } from './project-file-delete-dialog.component';

@Injectable()
export class ProjectFileResolvePagingParams implements Resolve<any> {

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

export const projectFileRoute: Routes = [
    {
        path: 'project-file',
        component: ProjectFileComponent,
        resolve: {
            'pagingParams': ProjectFileResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'rowadApp.projectFile.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'project-file/:id',
        component: ProjectFileDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'rowadApp.projectFile.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const projectFilePopupRoute: Routes = [
    {
        path: 'project-file-new',
        component: ProjectFilePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'rowadApp.projectFile.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'project-file/:id/edit',
        component: ProjectFilePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'rowadApp.projectFile.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'project-file/:id/delete',
        component: ProjectFileDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'rowadApp.projectFile.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
