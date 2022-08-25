import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISqueaks } from '../squeaks.model';
import { SqueaksService } from '../service/squeaks.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './squeaks-delete-dialog.component.html',
})
export class SqueaksDeleteDialogComponent {
  squeaks?: ISqueaks;

  constructor(protected squeaksService: SqueaksService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.squeaksService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
