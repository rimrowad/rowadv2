import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { RowadParameterModule } from './parameter/parameter.module';
import { RowadTeamModule } from './team/team.module';
import { RowadTeamMemberModule } from './team-member/team-member.module';
import { RowadInvestorModule } from './investor/investor.module';
import { RowadProjectFileModule } from './project-file/project-file.module';
import { RowadProjectEventModule } from './project-event/project-event.module';
import { RowadProjectModule } from './project/project.module';
import { RowadTeamInvitationModule } from './team-invitation/team-invitation.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        RowadParameterModule,
        RowadTeamModule,
        RowadTeamMemberModule,
        RowadInvestorModule,
        RowadProjectFileModule,
        RowadProjectEventModule,
        RowadProjectModule,
        RowadTeamInvitationModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RowadEntityModule {}
