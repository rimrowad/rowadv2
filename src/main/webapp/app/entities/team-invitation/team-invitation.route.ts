import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { TeamInvitationComponent } from './team-invitation.component';
import { TeamInvitationDetailComponent } from './team-invitation-detail.component';
import { TeamInvitationPopupComponent } from './team-invitation-dialog.component';
import { TeamInvitationDeletePopupComponent } from './team-invitation-delete-dialog.component';
import { TeamInvitationInboxComponent } from './team-invitation-inbox.component';

export const teamInvitationRoute: Routes = [
    {
        path: 'team-invitation',
        component: TeamInvitationComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'rowadApp.teamInvitation.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'team-invitation-inbox',
        component: TeamInvitationInboxComponent,
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
