/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { RowadTestModule } from '../../../test.module';
import { ProjectEventComponent } from '../../../../../../main/webapp/app/entities/project-event/project-event.component';
import { ProjectEventService } from '../../../../../../main/webapp/app/entities/project-event/project-event.service';
import { ProjectEvent } from '../../../../../../main/webapp/app/entities/project-event/project-event.model';

describe('Component Tests', () => {

    describe('ProjectEvent Management Component', () => {
        let comp: ProjectEventComponent;
        let fixture: ComponentFixture<ProjectEventComponent>;
        let service: ProjectEventService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [RowadTestModule],
                declarations: [ProjectEventComponent],
                providers: [
                    ProjectEventService
                ]
            })
            .overrideTemplate(ProjectEventComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ProjectEventComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProjectEventService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new ProjectEvent(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.projectEvents[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
