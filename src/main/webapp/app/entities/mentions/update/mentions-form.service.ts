import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IMentions, NewMentions } from '../mentions.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMentions for edit and NewMentionsFormGroupInput for create.
 */
type MentionsFormGroupInput = IMentions | PartialWithRequiredKeyOf<NewMentions>;

type MentionsFormDefaults = Pick<NewMentions, 'id'>;

type MentionsFormGroupContent = {
  id: FormControl<IMentions['id'] | NewMentions['id']>;
  handle: FormControl<IMentions['handle']>;
  squeaks: FormControl<IMentions['squeaks']>;
};

export type MentionsFormGroup = FormGroup<MentionsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MentionsFormService {
  createMentionsFormGroup(mentions: MentionsFormGroupInput = { id: null }): MentionsFormGroup {
    const mentionsRawValue = {
      ...this.getFormDefaults(),
      ...mentions,
    };
    return new FormGroup<MentionsFormGroupContent>({
      id: new FormControl(
        { value: mentionsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      handle: new FormControl(mentionsRawValue.handle, {
        validators: [Validators.required, Validators.minLength(2)],
      }),
      squeaks: new FormControl(mentionsRawValue.squeaks),
    });
  }

  getMentions(form: MentionsFormGroup): IMentions | NewMentions {
    return form.getRawValue() as IMentions | NewMentions;
  }

  resetForm(form: MentionsFormGroup, mentions: MentionsFormGroupInput): void {
    const mentionsRawValue = { ...this.getFormDefaults(), ...mentions };
    form.reset(
      {
        ...mentionsRawValue,
        id: { value: mentionsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): MentionsFormDefaults {
    return {
      id: null,
    };
  }
}
