/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { RowadTestModule } from '../../../test.module';
import { InvestorComponent } from '../../../../../../main/webapp/app/entities/investor/investor.component';
import { InvestorService } from '../../../../../../main/webapp/app/entities/investor/investor.service';
import { Investor } from '../../../../../../main/webapp/app/entities/investor/investor.model';

describe('Component Tests', () => {

    describe('Investor Management Component', () => {
        let comp: InvestorComponent;
        let fixture: ComponentFixture<InvestorComponent>;
        let service: InvestorService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [RowadTestModule],
                declarations: [InvestorComponent],
                providers: [
                    InvestorService
                ]
            })
            .overrideTemplate(InvestorComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(InvestorComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(InvestorService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Investor(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.investors[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
