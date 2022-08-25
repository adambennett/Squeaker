import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUtilizer } from '../utilizer.model';
import { UtilizerService } from '../service/utilizer.service';

@Injectable({ providedIn: 'root' })
export class UtilizerRoutingResolveService implements Resolve<IUtilizer | null> {
  constructor(protected service: UtilizerService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IUtilizer | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((utilizer: HttpResponse<IUtilizer>) => {
          if (utilizer.body) {
            return of(utilizer.body);
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
