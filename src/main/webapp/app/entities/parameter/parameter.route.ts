import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { ParameterComponent } from './parameter.component';
import { ParameterDetailComponent } from './parameter-detail.component';
import { ParameterPopupComponent } from './parameter-dialog.component';
import { ParameterDeletePopupComponent } from './parameter-delete-dialog.component';

@Injectable()
export class ParameterResolvePagingParams implements Resolve<any> {

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

export const parameterRoute: Routes = [
    {
        path: 'parameter',
        component: ParameterComponent,
        resolve: {
            'pagingParams': ParameterResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'rowadApp.parameter.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'parameter/:id',
        component: ParameterDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'rowadApp.parameter.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const parameterPopupRoute: Routes = [
    {
        path: 'parameter-new',
        component: ParameterPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'rowadApp.parameter.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'parameter/:id/edit',
        component: ParameterPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'rowadApp.parameter.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'parameter/:id/delete',
        component: ParameterDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'rowadApp.parameter.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
