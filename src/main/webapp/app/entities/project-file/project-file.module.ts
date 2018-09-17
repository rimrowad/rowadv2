import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { RowadSharedModule } from '../../shared';
import {
    ProjectFileService,
    ProjectFilePopupService,
    ProjectFileComponent,
    ProjectFileDetailComponent,
    ProjectFileDialogComponent,
    ProjectFilePopupComponent,
    ProjectFileDeletePopupComponent,
    ProjectFileDeleteDialogComponent,
    projectFileRoute,
    projectFilePopupRoute,
    ProjectFileResolvePagingParams,
} from '.';

const ENTITY_STATES = [
    ...projectFileRoute,
    ...projectFilePopupRoute,
];

@NgModule({
    imports: [
        RowadSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        ProjectFileComponent,
        ProjectFileDetailComponent,
        ProjectFileDialogComponent,
        ProjectFileDeleteDialogComponent,
        ProjectFilePopupComponent,
        ProjectFileDeletePopupComponent,
    ],
    entryComponents: [
        ProjectFileComponent,
        ProjectFileDialogComponent,
        ProjectFilePopupComponent,
        ProjectFileDeleteDialogComponent,
        ProjectFileDeletePopupComponent,
    ],
    providers: [
        ProjectFileService,
        ProjectFilePopupService,
        ProjectFileResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RowadProjectFileModule {}
