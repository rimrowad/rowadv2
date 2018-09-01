import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { InvestorComponent } from './investor.component';
import { InvestorDetailComponent } from './investor-detail.component';
import { InvestorPopupComponent } from './investor-dialog.component';
import { InvestorDeletePopupComponent } from './investor-delete-dialog.component';

@Injectable()
export class InvestorResolvePagingParams implements Resolve<any> {

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

export const investorRoute: Routes = [
    {
        path: 'investor',
        component: InvestorComponent,
        resolve: {
            'pagingParams': InvestorResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'rowadApp.investor.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'investor/:id',
        component: InvestorDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'rowadApp.investor.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const investorPopupRoute: Routes = [
    {
        path: 'investor-new',
        component: InvestorPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'rowadApp.investor.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'investor/:id/edit',
        component: InvestorPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'rowadApp.investor.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'investor/:id/delete',
        component: InvestorDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'rowadApp.investor.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
