/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { RowadTestModule } from '../../../test.module';
import { ProjectFileComponent } from '../../../../../../main/webapp/app/entities/project-file/project-file.component';
import { ProjectFileService } from '../../../../../../main/webapp/app/entities/project-file/project-file.service';
import { ProjectFile } from '../../../../../../main/webapp/app/entities/project-file/project-file.model';

describe('Component Tests', () => {

    describe('ProjectFile Management Component', () => {
        let comp: ProjectFileComponent;
        let fixture: ComponentFixture<ProjectFileComponent>;
        let service: ProjectFileService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [RowadTestModule],
                declarations: [ProjectFileComponent],
                providers: [
                    ProjectFileService
                ]
            })
            .overrideTemplate(ProjectFileComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ProjectFileComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProjectFileService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new ProjectFile(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.projectFiles[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
