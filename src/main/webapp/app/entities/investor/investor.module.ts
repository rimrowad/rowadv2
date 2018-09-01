import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { RowadSharedModule } from '../../shared';
import { RowadAdminModule } from '../../admin/admin.module';
import {
    InvestorService,
    InvestorPopupService,
    InvestorComponent,
    InvestorDetailComponent,
    InvestorDialogComponent,
    InvestorPopupComponent,
    InvestorDeletePopupComponent,
    InvestorDeleteDialogComponent,
    investorRoute,
    investorPopupRoute,
    InvestorResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...investorRoute,
    ...investorPopupRoute,
];

@NgModule({
    imports: [
        RowadSharedModule,
        RowadAdminModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        InvestorComponent,
        InvestorDetailComponent,
        InvestorDialogComponent,
        InvestorDeleteDialogComponent,
        InvestorPopupComponent,
        InvestorDeletePopupComponent,
    ],
    entryComponents: [
        InvestorComponent,
        InvestorDialogComponent,
        InvestorPopupComponent,
        InvestorDeleteDialogComponent,
        InvestorDeletePopupComponent,
    ],
    providers: [
        InvestorService,
        InvestorPopupService,
        InvestorResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RowadInvestorModule {}
