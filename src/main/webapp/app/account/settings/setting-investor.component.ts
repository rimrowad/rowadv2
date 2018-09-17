import { Component, OnInit, AfterViewInit, Renderer, ElementRef } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';

import { EMAIL_ALREADY_USED_TYPE, LOGIN_ALREADY_USED_TYPE, AccountService, JhiLanguageHelper } from '../../shared';
import { Investor } from '../../entities/investor';
import { SettingCache } from './setting.cache';
import { JhiEventManager } from 'ng-jhipster';

@Component({
    selector: 'jhi-setting-investor',
    templateUrl: './setting-investor.component.html'
})
export class SettingInvestorComponent implements OnInit, AfterViewInit {

    investor: Investor;
    success: boolean;
    error: string;
    errorEmailExists: string;
    errorUserExists: string;

    constructor(
        private cache: SettingCache,
        private accountService: AccountService,
        private elementRef: ElementRef,
        private renderer: Renderer,
        private eventManager: JhiEventManager,
        private languageHelper: JhiLanguageHelper
    ) {
    }

    ngOnInit() {
        this.success = false;
        this.accountService.getInvestor().subscribe((investor) => {
            this.investor = investor.body;
        }, (response) => this.investor = {});
    }

    ngAfterViewInit() {
        // this.renderer.invokeElementMethod(this.elementRef.nativeElement.querySelector('#login'), 'focus', []);
    }

    register() {
        this.cache.investor = this.investor;
        this.accountService.saveInvestor(this.cache.investorResult()).subscribe(() => {
            this.success = true;
            this.eventManager.broadcast({ name: 'profileCompleted', content: 'OK'});
        }, (response) => this.processError(response));
    }
    private processError(response: HttpErrorResponse) {
        this.success = null;
        if (response.status === 400 && response.error.type === LOGIN_ALREADY_USED_TYPE) {
            this.errorUserExists = 'ERROR';
        } else if (response.status === 400 && response.error.type === EMAIL_ALREADY_USED_TYPE) {
            this.errorEmailExists = 'ERROR';
        } else {
            this.error = 'ERROR';
        }
    }
    leftArrow() {
        if (this.languageHelper.isRTL()) {
            return 'fa fa-arrow-right';
        }
        return 'fa fa-arrow-left';
    }
}
