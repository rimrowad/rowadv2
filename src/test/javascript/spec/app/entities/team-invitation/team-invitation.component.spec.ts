/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { RowadTestModule } from '../../../test.module';
import { TeamInvitationComponent } from '../../../../../../main/webapp/app/entities/team-invitation/team-invitation.component';
import { TeamInvitationService } from '../../../../../../main/webapp/app/entities/team-invitation/team-invitation.service';
import { TeamInvitation } from '../../../../../../main/webapp/app/entities/team-invitation/team-invitation.model';

describe('Component Tests', () => {

    describe('TeamInvitation Management Component', () => {
        let comp: TeamInvitationComponent;
        let fixture: ComponentFixture<TeamInvitationComponent>;
        let service: TeamInvitationService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [RowadTestModule],
                declarations: [TeamInvitationComponent],
                providers: [
                    TeamInvitationService
                ]
            })
            .overrideTemplate(TeamInvitationComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TeamInvitationComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TeamInvitationService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new TeamInvitation(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.teamInvitations[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
