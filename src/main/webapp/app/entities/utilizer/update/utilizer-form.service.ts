import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IUtilizer, NewUtilizer } from '../utilizer.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUtilizer for edit and NewUtilizerFormGroupInput for create.
 */
type UtilizerFormGroupInput = IUtilizer | PartialWithRequiredKeyOf<NewUtilizer>;

type UtilizerFormDefaults = Pick<NewUtilizer, 'id'>;

type UtilizerFormGroupContent = {
  id: FormControl<IUtilizer['id'] | NewUtilizer['id']>;
  handle: FormControl<IUtilizer['handle']>;
  followers: FormControl<IUtilizer['followers']>;
  following: FormControl<IUtilizer['following']>;
  user: FormControl<IUtilizer['user']>;
};

export type UtilizerFormGroup = FormGroup<UtilizerFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UtilizerFormService {
  createUtilizerFormGroup(utilizer: UtilizerFormGroupInput = { id: null }): UtilizerFormGroup {
    const utilizerRawValue = {
      ...this.getFormDefaults(),
      ...utilizer,
    };
    return new FormGroup<UtilizerFormGroupContent>({
      id: new FormControl(
        { value: utilizerRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      handle: new FormControl(utilizerRawValue.handle, {
        validators: [Validators.required, Validators.minLength(2)],
      }),
      followers: new FormControl(utilizerRawValue.followers),
      following: new FormControl(utilizerRawValue.following),
      user: new FormControl(utilizerRawValue.user),
    });
  }

  getUtilizer(form: UtilizerFormGroup): IUtilizer | NewUtilizer {
    return form.getRawValue() as IUtilizer | NewUtilizer;
  }

  resetForm(form: UtilizerFormGroup, utilizer: UtilizerFormGroupInput): void {
    const utilizerRawValue = { ...this.getFormDefaults(), ...utilizer };
    form.reset(
      {
        ...utilizerRawValue,
        id: { value: utilizerRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): UtilizerFormDefaults {
    return {
      id: null,
    };
  }
}
