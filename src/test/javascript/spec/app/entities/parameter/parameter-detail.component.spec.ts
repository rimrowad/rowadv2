/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { RowadTestModule } from '../../../test.module';
import { ParameterDetailComponent } from '../../../../../../main/webapp/app/entities/parameter/parameter-detail.component';
import { ParameterService } from '../../../../../../main/webapp/app/entities/parameter/parameter.service';
import { Parameter } from '../../../../../../main/webapp/app/entities/parameter/parameter.model';

describe('Component Tests', () => {

    describe('Parameter Management Detail Component', () => {
        let comp: ParameterDetailComponent;
        let fixture: ComponentFixture<ParameterDetailComponent>;
        let service: ParameterService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [RowadTestModule],
                declarations: [ParameterDetailComponent],
                providers: [
                    ParameterService
                ]
            })
            .overrideTemplate(ParameterDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ParameterDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ParameterService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Parameter(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.parameter).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
