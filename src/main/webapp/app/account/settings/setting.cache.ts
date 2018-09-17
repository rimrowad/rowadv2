import { Injectable } from '@angular/core';
import { Investor } from '../../entities/investor';
import { TeamMember } from '../../entities/team-member';
import { JhiDateUtils } from 'ng-jhipster';

@Injectable()
export class SettingCache {
    settingsAccount: any;
    investor: Investor;
    member: TeamMember;
    constructor(private dateUtils: JhiDateUtils) { }

    investorResult() {
        this.settingsAccount.profileCompleted = true;
        return {
            'account': this.settingsAccount,
            'investor': this.convertInvestor(this.investor)
        };
    }

    memberResult() {
        this.settingsAccount.profileCompleted = true;
        return {
            'account': this.settingsAccount,
            'member': this.convertMember(this.member)
        };
    }

    private convertInvestor(investor: Investor): Investor {
        const copy: Investor = Object.assign({}, investor);
        copy.dateOfBirth = this.dateUtils
            .convertLocalDateToServer(investor.dateOfBirth);
        return copy;
    }

    private convertMember(teamMember: TeamMember): TeamMember {
        const copy: TeamMember = Object.assign({}, teamMember);
        copy.dateOfBirth = this.dateUtils
            .convertLocalDateToServer(teamMember.dateOfBirth);
        return copy;
    }
}
