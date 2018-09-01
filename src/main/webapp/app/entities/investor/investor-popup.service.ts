import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { Investor } from './investor.model';
import { InvestorService } from './investor.service';

@Injectable()
export class InvestorPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private investorService: InvestorService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.investorService.find(id)
                    .subscribe((investorResponse: HttpResponse<Investor>) => {
                        const investor: Investor = investorResponse.body;
                        if (investor.dateOfBirth) {
                            investor.dateOfBirth = {
                                year: investor.dateOfBirth.getFullYear(),
                                month: investor.dateOfBirth.getMonth() + 1,
                                day: investor.dateOfBirth.getDate()
                            };
                        }
                        this.ngbModalRef = this.investorModalRef(component, investor);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.investorModalRef(component, new Investor());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    investorModalRef(component: Component, investor: Investor): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.investor = investor;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
