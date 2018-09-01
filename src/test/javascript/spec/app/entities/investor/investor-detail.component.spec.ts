/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { RowadTestModule } from '../../../test.module';
import { InvestorDetailComponent } from '../../../../../../main/webapp/app/entities/investor/investor-detail.component';
import { InvestorService } from '../../../../../../main/webapp/app/entities/investor/investor.service';
import { Investor } from '../../../../../../main/webapp/app/entities/investor/investor.model';

describe('Component Tests', () => {

    describe('Investor Management Detail Component', () => {
        let comp: InvestorDetailComponent;
        let fixture: ComponentFixture<InvestorDetailComponent>;
        let service: InvestorService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [RowadTestModule],
                declarations: [InvestorDetailComponent],
                providers: [
                    InvestorService
                ]
            })
            .overrideTemplate(InvestorDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(InvestorDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(InvestorService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Investor(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.investor).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
