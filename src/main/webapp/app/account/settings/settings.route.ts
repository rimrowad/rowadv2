import { Route } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { SettingsComponent } from './settings.component';
import { SettingInvestorComponent } from './setting-investor.component';
import { SettingGuard } from './setting.guard';
import { SettingMemberComponent } from './setting-member.component';

export const settingsRoute: Route = {
    path: 'settings',
    component: SettingsComponent,
    data: {
        authorities: ['ROLE_USER', 'ROLE_INVESTOR', 'ROLE_NEW_INVESTOR'],
        pageTitle: 'global.menu.account.settings'
    },
    canActivate: [UserRouteAccessService]
};
export const settingInvestorRoute: Route = {
    path: 'setting-investor',
    component: SettingInvestorComponent,
    data: {
        authorities: [],
        pageTitle: 'global.menu.account.settings'
    },
    canActivate: [SettingGuard]
};
export const settingMemberRoute: Route = {
    path: 'setting-member',
    component: SettingMemberComponent,
    data: {
        authorities: [],
        pageTitle: 'global.menu.account.settings'
    },
    canActivate: [SettingGuard]
};
