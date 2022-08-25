import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { MentionsFormService, MentionsFormGroup } from './mentions-form.service';
import { IMentions } from '../mentions.model';
import { MentionsService } from '../service/mentions.service';
import { ISqueaks } from 'app/entities/squeaks/squeaks.model';
import { SqueaksService } from 'app/entities/squeaks/service/squeaks.service';

@Component({
  selector: 'jhi-mentions-update',
  templateUrl: './mentions-update.component.html',
})
export class MentionsUpdateComponent implements OnInit {
  isSaving = false;
  mentions: IMentions | null = null;

  squeaksSharedCollection: ISqueaks[] = [];

  editForm: MentionsFormGroup = this.mentionsFormService.createMentionsFormGroup();

  constructor(
    protected mentionsService: MentionsService,
    protected mentionsFormService: MentionsFormService,
    protected squeaksService: SqueaksService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareSqueaks = (o1: ISqueaks | null, o2: ISqueaks | null): boolean => this.squeaksService.compareSqueaks(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mentions }) => {
      this.mentions = mentions;
      if (mentions) {
        this.updateForm(mentions);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const mentions = this.mentionsFormService.getMentions(this.editForm);
    if (mentions.id !== null) {
      this.subscribeToSaveResponse(this.mentionsService.update(mentions));
    } else {
      this.subscribeToSaveResponse(this.mentionsService.create(mentions));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMentions>>): void {
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

  protected updateForm(mentions: IMentions): void {
    this.mentions = mentions;
    this.mentionsFormService.resetForm(this.editForm, mentions);

    this.squeaksSharedCollection = this.squeaksService.addSqueaksToCollectionIfMissing<ISqueaks>(
      this.squeaksSharedCollection,
      mentions.squeaks
    );
  }

  protected loadRelationshipsOptions(): void {
    this.squeaksService
      .query()
      .pipe(map((res: HttpResponse<ISqueaks[]>) => res.body ?? []))
      .pipe(map((squeaks: ISqueaks[]) => this.squeaksService.addSqueaksToCollectionIfMissing<ISqueaks>(squeaks, this.mentions?.squeaks)))
      .subscribe((squeaks: ISqueaks[]) => (this.squeaksSharedCollection = squeaks));
  }
}
