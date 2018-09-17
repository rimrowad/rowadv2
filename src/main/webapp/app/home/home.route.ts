import { Routes } from '@angular/router';

import { HomeComponent } from '.';
import { HomeUserComponent } from './home-user.component';
import { HomeInvestorComponent } from './home-investor.component';

export const HOME_ROUTE: Routes = [
    {
        path: '',
        component: HomeComponent,
        data: {
            authorities: [],
            pageTitle: 'home.title'
        }
    },
    {
        path: 'home-user',
        component: HomeUserComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'home.user.title'
        }
    },
    {
        path: 'home-investor',
        component: HomeInvestorComponent,
        data: {
            authorities: ['ROLE_INVESTOR'],
            pageTitle: 'home.investor.title'
        }
    }
];
