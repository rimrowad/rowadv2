import { Directive, Input, TemplateRef, ViewContainerRef } from '@angular/core';
import { Principal } from './principal.service';
import { Account } from '../user/account.model';

/**
 * @whatItDoes Conditionally includes an HTML element if current user has any
 * of the authorities passed as the `expression`.
 *
 * @howToUse
 * ```
 *     <some-element *jhiHasAnyAuthority="'ROLE_ADMIN'">...</some-element>
 *
 *     <some-element *jhiHasAnyAuthority="['ROLE_ADMIN', 'ROLE_USER']">...</some-element>
 * ```
 */
@Directive({
    selector: '[jhiHasAnyAuthority]'
})
export class HasAnyAuthorityDirective {

    private authorities: string[];
    private account: Account;

    constructor(private principal: Principal, private templateRef: TemplateRef<any>, private viewContainerRef: ViewContainerRef) {
    }

    @Input()
    set jhiHasAnyAuthority(value: string|string[]) {
        this.authorities = typeof value === 'string' ? [ <string> value ] : <string[]> value;
        this.updateView();
        this.principal.identity().then((account) => {
            this.account = account;
        });
        // Get notified each time authentication state changes.
        this.principal.getAuthenticationState().subscribe((identity) => this.updateView());
    }

    private updateView(): void {
        this.principal.identity().then((account) => {
            this.account = account;
        });
        this.principal.hasAnyAuthority(this.authorities).then((result) => {
            this.viewContainerRef.clear();
            if (result && this.account.profileCompleted) {
                this.viewContainerRef.createEmbeddedView(this.templateRef);
            }
        });
    }
}
