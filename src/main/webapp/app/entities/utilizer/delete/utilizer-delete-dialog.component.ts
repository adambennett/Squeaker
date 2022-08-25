import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IUtilizer } from '../utilizer.model';
import { UtilizerService } from '../service/utilizer.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './utilizer-delete-dialog.component.html',
})
export class UtilizerDeleteDialogComponent {
  utilizer?: IUtilizer;

  constructor(protected utilizerService: UtilizerService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.utilizerService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
