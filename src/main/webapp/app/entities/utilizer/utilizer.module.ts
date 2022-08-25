import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { UtilizerComponent } from './list/utilizer.component';
import { UtilizerDetailComponent } from './detail/utilizer-detail.component';
import { UtilizerUpdateComponent } from './update/utilizer-update.component';
import { UtilizerDeleteDialogComponent } from './delete/utilizer-delete-dialog.component';
import { UtilizerRoutingModule } from './route/utilizer-routing.module';

@NgModule({
  imports: [SharedModule, UtilizerRoutingModule],
  declarations: [UtilizerComponent, UtilizerDetailComponent, UtilizerUpdateComponent, UtilizerDeleteDialogComponent],
})
export class UtilizerModule {}
