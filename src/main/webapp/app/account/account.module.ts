import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { RowadSharedModule } from '../shared';

import {
    Register,
    ActivateService,
    PasswordService,
    PasswordResetInitService,
    PasswordResetFinishService,
    PasswordStrengthBarComponent,
    RegisterComponent,
    ActivateComponent,
    PasswordComponent,
    PasswordResetInitComponent,
    PasswordResetFinishComponent,
    SettingsComponent,
    accountState
} from '.';
import { SettingCache } from './settings/setting.cache';
import { SettingGuard } from './settings/setting.guard';
import { SettingInvestorComponent } from './settings/setting-investor.component';
import { SettingMemberComponent } from './settings/setting-member.component';
import { RegisterType } from './register/register-type.service';

@NgModule({
    imports: [
        RowadSharedModule,
        RouterModule.forChild(accountState)
    ],
    declarations: [
        ActivateComponent,
        RegisterComponent,
        SettingInvestorComponent,
        SettingMemberComponent,
        PasswordComponent,
        PasswordStrengthBarComponent,
        PasswordResetInitComponent,
        PasswordResetFinishComponent,
        SettingsComponent
    ],
    providers: [
        Register,
        RegisterType,
        SettingCache,
        SettingGuard,
        ActivateService,
        PasswordService,
        PasswordResetInitService,
        PasswordResetFinishService
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RowadAccountModule {}
