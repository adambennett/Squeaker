import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { UtilizerComponent } from '../list/utilizer.component';
import { UtilizerDetailComponent } from '../detail/utilizer-detail.component';
import { UtilizerUpdateComponent } from '../update/utilizer-update.component';
import { UtilizerRoutingResolveService } from './utilizer-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const utilizerRoute: Routes = [
  {
    path: '',
    component: UtilizerComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UtilizerDetailComponent,
    resolve: {
      utilizer: UtilizerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UtilizerUpdateComponent,
    resolve: {
      utilizer: UtilizerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UtilizerUpdateComponent,
    resolve: {
      utilizer: UtilizerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(utilizerRoute)],
  exports: [RouterModule],
})
export class UtilizerRoutingModule {}
