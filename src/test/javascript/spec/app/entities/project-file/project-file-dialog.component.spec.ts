/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { RowadTestModule } from '../../../test.module';
import { ProjectFileDialogComponent } from '../../../../../../main/webapp/app/entities/project-file/project-file-dialog.component';
import { ProjectFileService } from '../../../../../../main/webapp/app/entities/project-file/project-file.service';
import { ProjectFile } from '../../../../../../main/webapp/app/entities/project-file/project-file.model';
import { ProjectService } from '../../../../../../main/webapp/app/entities/project';

describe('Component Tests', () => {

    describe('ProjectFile Management Dialog Component', () => {
        let comp: ProjectFileDialogComponent;
        let fixture: ComponentFixture<ProjectFileDialogComponent>;
        let service: ProjectFileService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [RowadTestModule],
                declarations: [ProjectFileDialogComponent],
                providers: [
                    ProjectService,
                    ProjectFileService
                ]
            })
            .overrideTemplate(ProjectFileDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ProjectFileDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProjectFileService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new ProjectFile(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.projectFile = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'projectFileListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new ProjectFile();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.projectFile = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'projectFileListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
