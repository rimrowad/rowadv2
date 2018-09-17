/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { RowadTestModule } from '../../../test.module';
import { TeamInvitationDialogComponent } from '../../../../../../main/webapp/app/entities/team-invitation/team-invitation-dialog.component';
import { TeamInvitationService } from '../../../../../../main/webapp/app/entities/team-invitation/team-invitation.service';
import { TeamInvitation } from '../../../../../../main/webapp/app/entities/team-invitation/team-invitation.model';
import { TeamMemberService } from '../../../../../../main/webapp/app/entities/team-member';
import { TeamService } from '../../../../../../main/webapp/app/entities/team';

describe('Component Tests', () => {

    describe('TeamInvitation Management Dialog Component', () => {
        let comp: TeamInvitationDialogComponent;
        let fixture: ComponentFixture<TeamInvitationDialogComponent>;
        let service: TeamInvitationService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [RowadTestModule],
                declarations: [TeamInvitationDialogComponent],
                providers: [
                    TeamMemberService,
                    TeamService,
                    TeamInvitationService
                ]
            })
            .overrideTemplate(TeamInvitationDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TeamInvitationDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TeamInvitationService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new TeamInvitation(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.teamInvitation = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'teamInvitationListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new TeamInvitation();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.teamInvitation = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'teamInvitationListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
