/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { RowadTestModule } from '../../../test.module';
import { ParameterComponent } from '../../../../../../main/webapp/app/entities/parameter/parameter.component';
import { ParameterService } from '../../../../../../main/webapp/app/entities/parameter/parameter.service';
import { Parameter } from '../../../../../../main/webapp/app/entities/parameter/parameter.model';

describe('Component Tests', () => {

    describe('Parameter Management Component', () => {
        let comp: ParameterComponent;
        let fixture: ComponentFixture<ParameterComponent>;
        let service: ParameterService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [RowadTestModule],
                declarations: [ParameterComponent],
                providers: [
                    ParameterService
                ]
            })
            .overrideTemplate(ParameterComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ParameterComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ParameterService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Parameter(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.parameters[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
