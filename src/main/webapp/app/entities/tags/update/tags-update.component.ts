import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { TagsFormService, TagsFormGroup } from './tags-form.service';
import { ITags } from '../tags.model';
import { TagsService } from '../service/tags.service';
import { ISqueaks } from 'app/entities/squeaks/squeaks.model';
import { SqueaksService } from 'app/entities/squeaks/service/squeaks.service';

@Component({
  selector: 'jhi-tags-update',
  templateUrl: './tags-update.component.html',
})
export class TagsUpdateComponent implements OnInit {
  isSaving = false;
  tags: ITags | null = null;

  squeaksSharedCollection: ISqueaks[] = [];

  editForm: TagsFormGroup = this.tagsFormService.createTagsFormGroup();

  constructor(
    protected tagsService: TagsService,
    protected tagsFormService: TagsFormService,
    protected squeaksService: SqueaksService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareSqueaks = (o1: ISqueaks | null, o2: ISqueaks | null): boolean => this.squeaksService.compareSqueaks(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tags }) => {
      this.tags = tags;
      if (tags) {
        this.updateForm(tags);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tags = this.tagsFormService.getTags(this.editForm);
    if (tags.id !== null) {
      this.subscribeToSaveResponse(this.tagsService.update(tags));
    } else {
      this.subscribeToSaveResponse(this.tagsService.create(tags));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITags>>): void {
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

  protected updateForm(tags: ITags): void {
    this.tags = tags;
    this.tagsFormService.resetForm(this.editForm, tags);

    this.squeaksSharedCollection = this.squeaksService.addSqueaksToCollectionIfMissing<ISqueaks>(
      this.squeaksSharedCollection,
      tags.squeaks
    );
  }

  protected loadRelationshipsOptions(): void {
    this.squeaksService
      .query()
      .pipe(map((res: HttpResponse<ISqueaks[]>) => res.body ?? []))
      .pipe(map((squeaks: ISqueaks[]) => this.squeaksService.addSqueaksToCollectionIfMissing<ISqueaks>(squeaks, this.tags?.squeaks)))
      .subscribe((squeaks: ISqueaks[]) => (this.squeaksSharedCollection = squeaks));
  }
}
