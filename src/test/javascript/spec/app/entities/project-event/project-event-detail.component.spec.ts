/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { RowadTestModule } from '../../../test.module';
import { ProjectEventDetailComponent } from '../../../../../../main/webapp/app/entities/project-event/project-event-detail.component';
import { ProjectEventService } from '../../../../../../main/webapp/app/entities/project-event/project-event.service';
import { ProjectEvent } from '../../../../../../main/webapp/app/entities/project-event/project-event.model';

describe('Component Tests', () => {

    describe('ProjectEvent Management Detail Component', () => {
        let comp: ProjectEventDetailComponent;
        let fixture: ComponentFixture<ProjectEventDetailComponent>;
        let service: ProjectEventService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [RowadTestModule],
                declarations: [ProjectEventDetailComponent],
                providers: [
                    ProjectEventService
                ]
            })
            .overrideTemplate(ProjectEventDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ProjectEventDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProjectEventService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new ProjectEvent(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.projectEvent).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
