import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { RowadSharedModule } from '../../shared';
import {
    TeamInvitationService,
    TeamInvitationPopupService,
    TeamInvitationComponent,
    TeamInvitationDetailComponent,
    TeamInvitationDialogComponent,
    TeamInvitationPopupComponent,
    TeamInvitationDeletePopupComponent,
    TeamInvitationDeleteDialogComponent,
    teamInvitationRoute,
    teamInvitationPopupRoute,
} from '.';
import { TeamInvitationInboxComponent } from './team-invitation-inbox.component';

const ENTITY_STATES = [
    ...teamInvitationRoute,
    ...teamInvitationPopupRoute,
];

@NgModule({
    imports: [
        RowadSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        TeamInvitationComponent,
        TeamInvitationInboxComponent,
        TeamInvitationDetailComponent,
        TeamInvitationDialogComponent,
        TeamInvitationDeleteDialogComponent,
        TeamInvitationPopupComponent,
        TeamInvitationDeletePopupComponent,
    ],
    entryComponents: [
        TeamInvitationComponent,
        TeamInvitationDialogComponent,
        TeamInvitationPopupComponent,
        TeamInvitationDeleteDialogComponent,
        TeamInvitationDeletePopupComponent,
    ],
    providers: [
        TeamInvitationService,
        TeamInvitationPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    exports: [
        TeamInvitationComponent,
        TeamInvitationInboxComponent,
    ]
})
export class RowadTeamInvitationModule {}
