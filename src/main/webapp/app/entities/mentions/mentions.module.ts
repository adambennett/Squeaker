import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MentionsComponent } from './list/mentions.component';
import { MentionsDetailComponent } from './detail/mentions-detail.component';
import { MentionsUpdateComponent } from './update/mentions-update.component';
import { MentionsDeleteDialogComponent } from './delete/mentions-delete-dialog.component';
import { MentionsRoutingModule } from './route/mentions-routing.module';

@NgModule({
  imports: [SharedModule, MentionsRoutingModule],
  declarations: [MentionsComponent, MentionsDetailComponent, MentionsUpdateComponent, MentionsDeleteDialogComponent],
})
export class MentionsModule {}
