/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { RowadTestModule } from '../../../test.module';
import { TeamMemberDetailComponent } from '../../../../../../main/webapp/app/entities/team-member/team-member-detail.component';
import { TeamMemberService } from '../../../../../../main/webapp/app/entities/team-member/team-member.service';
import { TeamMember } from '../../../../../../main/webapp/app/entities/team-member/team-member.model';

describe('Component Tests', () => {

    describe('TeamMember Management Detail Component', () => {
        let comp: TeamMemberDetailComponent;
        let fixture: ComponentFixture<TeamMemberDetailComponent>;
        let service: TeamMemberService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [RowadTestModule],
                declarations: [TeamMemberDetailComponent],
                providers: [
                    TeamMemberService
                ]
            })
            .overrideTemplate(TeamMemberDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TeamMemberDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TeamMemberService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new TeamMember(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.teamMember).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
