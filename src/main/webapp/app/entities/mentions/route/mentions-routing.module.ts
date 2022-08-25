import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MentionsComponent } from '../list/mentions.component';
import { MentionsDetailComponent } from '../detail/mentions-detail.component';
import { MentionsUpdateComponent } from '../update/mentions-update.component';
import { MentionsRoutingResolveService } from './mentions-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const mentionsRoute: Routes = [
  {
    path: '',
    component: MentionsComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MentionsDetailComponent,
    resolve: {
      mentions: MentionsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MentionsUpdateComponent,
    resolve: {
      mentions: MentionsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MentionsUpdateComponent,
    resolve: {
      mentions: MentionsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(mentionsRoute)],
  exports: [RouterModule],
})
export class MentionsRoutingModule {}
