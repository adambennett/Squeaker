import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISqueaks } from '../squeaks.model';
import { SqueaksService } from '../service/squeaks.service';

@Injectable({ providedIn: 'root' })
export class SqueaksRoutingResolveService implements Resolve<ISqueaks | null> {
  constructor(protected service: SqueaksService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISqueaks | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((squeaks: HttpResponse<ISqueaks>) => {
          if (squeaks.body) {
            return of(squeaks.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
