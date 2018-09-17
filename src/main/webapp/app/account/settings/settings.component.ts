import { Component, OnInit } from '@angular/core';
import { JhiLanguageService } from 'ng-jhipster';

import { Principal, AccountService, JhiLanguageHelper } from '../../shared';
import { SettingCache } from './setting.cache';
import { Router } from '@angular/router';

@Component({
    selector: 'jhi-settings',
    templateUrl: './settings.component.html'
})
export class SettingsComponent implements OnInit {
    error: string;
    success: string;
    settingsAccount: any;
    languages: any[];
    isInvestor: boolean;

    constructor(
        private account: AccountService,
        private principal: Principal,
        private settingCache: SettingCache,
        private router: Router,
        private languageService: JhiLanguageService,
        private languageHelper: JhiLanguageHelper
    ) {
    }

    ngOnInit() {
        this.principal.identity().then((account) => {
            this.settingsAccount = this.copyAccount(account);
        });
        this.languageHelper.getAll().then((languages) => {
            this.languages = languages;
        });
        this.principal.hasAnyAuthority(['ROLE_INVESTOR', 'ROLE_NEW_INVESTOR']).then((rep) => {
            this.isInvestor = rep;
        });
    }

    save() {
        this.settingCache.settingsAccount = this.settingsAccount;
        if (this.isInvestor) {
            this.router.navigate(['/setting-investor']);
        } else {
            this.router.navigate(['/setting-member']);
        }
        // this.account.save(this.settingsAccount).subscribe(() => {
        //     this.error = null;
        //     this.success = 'OK';
        //     this.principal.identity(true).then((account) => {
        //         this.settingsAccount = this.copyAccount(account);
        //     });
        //     this.languageService.getCurrent().then((current) => {
        //         if (this.settingsAccount.langKey !== current) {
        //             this.languageService.changeLanguage(this.settingsAccount.langKey);
        //         }
        //     });
        // }, () => {
        //     this.success = null;
        //     this.error = 'ERROR';
        // });
    }

    copyAccount(account) {
        return {
            activated: account.activated,
            email: account.email,
            firstName: account.firstName,
            langKey: account.langKey,
            lastName: account.lastName,
            login: account.login,
            imageUrl: account.imageUrl
        };
    }

    rightArrow() {
        if (this.languageHelper.isRTL()) {
            return 'fa fa-arrow-left';
        }
        return 'fa fa-arrow-right';
    }
}
