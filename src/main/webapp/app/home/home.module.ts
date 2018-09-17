import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { RowadSharedModule } from '../shared';

import { HOME_ROUTE, HomeComponent } from '.';
import { HomeUserComponent } from './home-user.component';
import { HomeInvestorComponent } from './home-investor.component';
import { TeamInvitationComponent, TeamInvitationInboxComponent } from '../entities/team-invitation';
import { RowadTeamInvitationModule } from '../entities/team-invitation/team-invitation.module';

@NgModule({
    imports: [
        RowadSharedModule,
        RowadTeamInvitationModule,
        RouterModule.forChild(HOME_ROUTE)
    ],
    declarations: [
        HomeComponent,
        HomeUserComponent,
        HomeInvestorComponent,
        //TeamInvitationComponent,
        //TeamInvitationInboxComponent
    ],
    entryComponents: [
    ],
    providers: [
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RowadHomeModule {}
