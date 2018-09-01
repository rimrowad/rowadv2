import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { RowadSharedModule } from '../../shared';
import {
    ParameterService,
    ParameterPopupService,
    ParameterComponent,
    ParameterDetailComponent,
    ParameterDialogComponent,
    ParameterPopupComponent,
    ParameterDeletePopupComponent,
    ParameterDeleteDialogComponent,
    parameterRoute,
    parameterPopupRoute,
    ParameterResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...parameterRoute,
    ...parameterPopupRoute,
];

@NgModule({
    imports: [
        RowadSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        ParameterComponent,
        ParameterDetailComponent,
        ParameterDialogComponent,
        ParameterDeleteDialogComponent,
        ParameterPopupComponent,
        ParameterDeletePopupComponent,
    ],
    entryComponents: [
        ParameterComponent,
        ParameterDialogComponent,
        ParameterPopupComponent,
        ParameterDeleteDialogComponent,
        ParameterDeletePopupComponent,
    ],
    providers: [
        ParameterService,
        ParameterPopupService,
        ParameterResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RowadParameterModule {}
