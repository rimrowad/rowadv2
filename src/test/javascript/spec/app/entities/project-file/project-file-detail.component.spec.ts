/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { RowadTestModule } from '../../../test.module';
import { ProjectFileDetailComponent } from '../../../../../../main/webapp/app/entities/project-file/project-file-detail.component';
import { ProjectFileService } from '../../../../../../main/webapp/app/entities/project-file/project-file.service';
import { ProjectFile } from '../../../../../../main/webapp/app/entities/project-file/project-file.model';

describe('Component Tests', () => {

    describe('ProjectFile Management Detail Component', () => {
        let comp: ProjectFileDetailComponent;
        let fixture: ComponentFixture<ProjectFileDetailComponent>;
        let service: ProjectFileService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [RowadTestModule],
                declarations: [ProjectFileDetailComponent],
                providers: [
                    ProjectFileService
                ]
            })
            .overrideTemplate(ProjectFileDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ProjectFileDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProjectFileService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new ProjectFile(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.projectFile).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
