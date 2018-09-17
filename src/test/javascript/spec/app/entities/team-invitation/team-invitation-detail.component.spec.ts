/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { RowadTestModule } from '../../../test.module';
import { TeamInvitationDetailComponent } from '../../../../../../main/webapp/app/entities/team-invitation/team-invitation-detail.component';
import { TeamInvitationService } from '../../../../../../main/webapp/app/entities/team-invitation/team-invitation.service';
import { TeamInvitation } from '../../../../../../main/webapp/app/entities/team-invitation/team-invitation.model';

describe('Component Tests', () => {

    describe('TeamInvitation Management Detail Component', () => {
        let comp: TeamInvitationDetailComponent;
        let fixture: ComponentFixture<TeamInvitationDetailComponent>;
        let service: TeamInvitationService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [RowadTestModule],
                declarations: [TeamInvitationDetailComponent],
                providers: [
                    TeamInvitationService
                ]
            })
            .overrideTemplate(TeamInvitationDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TeamInvitationDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TeamInvitationService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new TeamInvitation(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.teamInvitation).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
