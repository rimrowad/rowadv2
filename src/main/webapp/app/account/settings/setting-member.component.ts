import { Component, OnInit, AfterViewInit, Renderer, ElementRef } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';

import { EMAIL_ALREADY_USED_TYPE, LOGIN_ALREADY_USED_TYPE, AccountService, JhiLanguageHelper } from '../../shared';
import { SettingCache } from './setting.cache';
import { TeamMember } from '../../entities/team-member';
import { Router } from '@angular/router';
import { JhiEventManager } from 'ng-jhipster';

@Component({
    selector: 'jhi-settings-member',
    templateUrl: './setting-member.component.html'
})
export class SettingMemberComponent implements OnInit, AfterViewInit {

    teamMember: TeamMember;
    success: boolean;
    error: string;
    errorEmailExists: string;
    errorUserExists: string;

    constructor(
        private cache: SettingCache,
        private accountService: AccountService,
        private router: Router,
        private eventManager: JhiEventManager,
        private languageHelper: JhiLanguageHelper
    ) {
    }

    ngOnInit() {
        this.success = false;
        this.accountService.getMember().subscribe((member) => {
            this.teamMember = member.body;
        }, (response) => this.teamMember = {});
    }

    ngAfterViewInit() {
        // this.renderer.invokeElementMethod(this.elementRef.nativeElement.querySelector('#login'), 'focus', []);
    }

    register() {
        this.cache.member = this.teamMember;
        this.accountService.saveMember(this.cache.memberResult()).subscribe(() => {
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
    goToPrevious() {
        this.router.navigate(['/settings']);
    }
    leftArrow() {
        if (this.languageHelper.isRTL()) {
            return 'fa fa-arrow-right';
        }
        return 'fa fa-arrow-left';
    }
}
