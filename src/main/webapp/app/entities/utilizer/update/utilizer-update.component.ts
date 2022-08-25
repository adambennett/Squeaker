import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { UtilizerFormService, UtilizerFormGroup } from './utilizer-form.service';
import { IUtilizer } from '../utilizer.model';
import { UtilizerService } from '../service/utilizer.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-utilizer-update',
  templateUrl: './utilizer-update.component.html',
})
export class UtilizerUpdateComponent implements OnInit {
  isSaving = false;
  utilizer: IUtilizer | null = null;

  usersSharedCollection: IUser[] = [];

  editForm: UtilizerFormGroup = this.utilizerFormService.createUtilizerFormGroup();

  constructor(
    protected utilizerService: UtilizerService,
    protected utilizerFormService: UtilizerFormService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ utilizer }) => {
      this.utilizer = utilizer;
      if (utilizer) {
        this.updateForm(utilizer);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const utilizer = this.utilizerFormService.getUtilizer(this.editForm);
    if (utilizer.id !== null) {
      this.subscribeToSaveResponse(this.utilizerService.update(utilizer));
    } else {
      this.subscribeToSaveResponse(this.utilizerService.create(utilizer));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUtilizer>>): void {
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

  protected updateForm(utilizer: IUtilizer): void {
    this.utilizer = utilizer;
    this.utilizerFormService.resetForm(this.editForm, utilizer);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, utilizer.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.utilizer?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
