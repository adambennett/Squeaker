import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SqueaksComponent } from '../list/squeaks.component';
import { SqueaksDetailComponent } from '../detail/squeaks-detail.component';
import { SqueaksUpdateComponent } from '../update/squeaks-update.component';
import { SqueaksRoutingResolveService } from './squeaks-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const squeaksRoute: Routes = [
  {
    path: '',
    component: SqueaksComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SqueaksDetailComponent,
    resolve: {
      squeaks: SqueaksRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SqueaksUpdateComponent,
    resolve: {
      squeaks: SqueaksRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SqueaksUpdateComponent,
    resolve: {
      squeaks: SqueaksRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(squeaksRoute)],
  exports: [RouterModule],
})
export class SqueaksRoutingModule {}
