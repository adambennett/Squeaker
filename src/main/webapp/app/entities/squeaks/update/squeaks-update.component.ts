import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { SqueaksFormService, SqueaksFormGroup } from './squeaks-form.service';
import { ISqueaks } from '../squeaks.model';
import { SqueaksService } from '../service/squeaks.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IUtilizer } from 'app/entities/utilizer/utilizer.model';
import { UtilizerService } from 'app/entities/utilizer/service/utilizer.service';

@Component({
  selector: 'jhi-squeaks-update',
  templateUrl: './squeaks-update.component.html',
})
export class SqueaksUpdateComponent implements OnInit {
  isSaving = false;
  squeaks: ISqueaks | null = null;

  utilizersSharedCollection: IUtilizer[] = [];

  editForm: SqueaksFormGroup = this.squeaksFormService.createSqueaksFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected squeaksService: SqueaksService,
    protected squeaksFormService: SqueaksFormService,
    protected utilizerService: UtilizerService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareUtilizer = (o1: IUtilizer | null, o2: IUtilizer | null): boolean => this.utilizerService.compareUtilizer(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ squeaks }) => {
      this.squeaks = squeaks;
      if (squeaks) {
        this.updateForm(squeaks);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('squeakerApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const squeaks = this.squeaksFormService.getSqueaks(this.editForm);
    if (squeaks.id !== null) {
      this.subscribeToSaveResponse(this.squeaksService.update(squeaks));
    } else {
      this.subscribeToSaveResponse(this.squeaksService.create(squeaks));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISqueaks>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(squeaks: ISqueaks): void {
    this.squeaks = squeaks;
    this.squeaksFormService.resetForm(this.editForm, squeaks);

    this.utilizersSharedCollection = this.utilizerService.addUtilizerToCollectionIfMissing<IUtilizer>(
      this.utilizersSharedCollection,
      squeaks.utilizer
    );
  }

  protected loadRelationshipsOptions(): void {
    this.utilizerService
      .query()
      .pipe(map((res: HttpResponse<IUtilizer[]>) => res.body ?? []))
      .pipe(
        map((utilizers: IUtilizer[]) => this.utilizerService.addUtilizerToCollectionIfMissing<IUtilizer>(utilizers, this.squeaks?.utilizer))
      )
      .subscribe((utilizers: IUtilizer[]) => (this.utilizersSharedCollection = utilizers));
  }
}
