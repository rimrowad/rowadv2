import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { RowadSharedModule } from '../../shared';
import { RowadAdminModule } from '../../admin/admin.module';
import {
    ProjectEventService,
    ProjectEventPopupService,
    ProjectEventComponent,
    ProjectEventDetailComponent,
    ProjectEventDialogComponent,
    ProjectEventPopupComponent,
    ProjectEventDeletePopupComponent,
    ProjectEventDeleteDialogComponent,
    projectEventRoute,
    projectEventPopupRoute,
    ProjectEventResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...projectEventRoute,
    ...projectEventPopupRoute,
];

@NgModule({
    imports: [
        RowadSharedModule,
        RowadAdminModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        ProjectEventComponent,
        ProjectEventDetailComponent,
        ProjectEventDialogComponent,
        ProjectEventDeleteDialogComponent,
        ProjectEventPopupComponent,
        ProjectEventDeletePopupComponent,
    ],
    entryComponents: [
        ProjectEventComponent,
        ProjectEventDialogComponent,
        ProjectEventPopupComponent,
        ProjectEventDeleteDialogComponent,
        ProjectEventDeletePopupComponent,
    ],
    providers: [
        ProjectEventService,
        ProjectEventPopupService,
        ProjectEventResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RowadProjectEventModule {}
