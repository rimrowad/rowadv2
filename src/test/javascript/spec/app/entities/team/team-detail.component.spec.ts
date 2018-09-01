/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { RowadTestModule } from '../../../test.module';
import { TeamDetailComponent } from '../../../../../../main/webapp/app/entities/team/team-detail.component';
import { TeamService } from '../../../../../../main/webapp/app/entities/team/team.service';
import { Team } from '../../../../../../main/webapp/app/entities/team/team.model';

describe('Component Tests', () => {

    describe('Team Management Detail Component', () => {
        let comp: TeamDetailComponent;
        let fixture: ComponentFixture<TeamDetailComponent>;
        let service: TeamService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [RowadTestModule],
                declarations: [TeamDetailComponent],
                providers: [
                    TeamService
                ]
            })
            .overrideTemplate(TeamDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TeamDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TeamService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Team(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.team).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
