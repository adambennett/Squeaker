import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SqueaksComponent } from './list/squeaks.component';
import { SqueaksDetailComponent } from './detail/squeaks-detail.component';
import { SqueaksUpdateComponent } from './update/squeaks-update.component';
import { SqueaksDeleteDialogComponent } from './delete/squeaks-delete-dialog.component';
import { SqueaksRoutingModule } from './route/squeaks-routing.module';

@NgModule({
  imports: [SharedModule, SqueaksRoutingModule],
  declarations: [SqueaksComponent, SqueaksDetailComponent, SqueaksUpdateComponent, SqueaksDeleteDialogComponent],
})
export class SqueaksModule {}
