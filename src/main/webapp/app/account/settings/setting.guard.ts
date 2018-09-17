import { Injectable } from '@angular/core';
import {
    CanActivate, Router,
    ActivatedRouteSnapshot,
    RouterStateSnapshot,
} from '@angular/router';
import { SettingCache } from './setting.cache';

@Injectable()
export class SettingGuard implements CanActivate {
    constructor(private router: Router, private cache: SettingCache) { }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
        if (! this.cache.settingsAccount ) {
            this.router.navigate(['/settings']);
            return false;
        }
        return true;
    }
}
