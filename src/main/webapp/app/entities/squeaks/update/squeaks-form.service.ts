import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ISqueaks, NewSqueaks } from '../squeaks.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISqueaks for edit and NewSqueaksFormGroupInput for create.
 */
type SqueaksFormGroupInput = ISqueaks | PartialWithRequiredKeyOf<NewSqueaks>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ISqueaks | NewSqueaks> = Omit<T, 'createdAt'> & {
  createdAt?: string | null;
};

type SqueaksFormRawValue = FormValueOf<ISqueaks>;

type NewSqueaksFormRawValue = FormValueOf<NewSqueaks>;

type SqueaksFormDefaults = Pick<NewSqueaks, 'id' | 'createdAt'>;

type SqueaksFormGroupContent = {
  id: FormControl<SqueaksFormRawValue['id'] | NewSqueaks['id']>;
  content: FormControl<SqueaksFormRawValue['content']>;
  createdAt: FormControl<SqueaksFormRawValue['createdAt']>;
  likes: FormControl<SqueaksFormRawValue['likes']>;
  image: FormControl<SqueaksFormRawValue['image']>;
  imageContentType: FormControl<SqueaksFormRawValue['imageContentType']>;
  video: FormControl<SqueaksFormRawValue['video']>;
  videoContentType: FormControl<SqueaksFormRawValue['videoContentType']>;
  utilizer: FormControl<SqueaksFormRawValue['utilizer']>;
};

export type SqueaksFormGroup = FormGroup<SqueaksFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SqueaksFormService {
  createSqueaksFormGroup(squeaks: SqueaksFormGroupInput = { id: null }): SqueaksFormGroup {
    const squeaksRawValue = this.convertSqueaksToSqueaksRawValue({
      ...this.getFormDefaults(),
      ...squeaks,
    });
    return new FormGroup<SqueaksFormGroupContent>({
      id: new FormControl(
        { value: squeaksRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      content: new FormControl(squeaksRawValue.content, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      createdAt: new FormControl(squeaksRawValue.createdAt),
      likes: new FormControl(squeaksRawValue.likes),
      image: new FormControl(squeaksRawValue.image),
      imageContentType: new FormControl(squeaksRawValue.imageContentType),
      video: new FormControl(squeaksRawValue.video),
      videoContentType: new FormControl(squeaksRawValue.videoContentType),
      utilizer: new FormControl(squeaksRawValue.utilizer),
    });
  }

  getSqueaks(form: SqueaksFormGroup): ISqueaks | NewSqueaks {
    return this.convertSqueaksRawValueToSqueaks(form.getRawValue() as SqueaksFormRawValue | NewSqueaksFormRawValue);
  }

  resetForm(form: SqueaksFormGroup, squeaks: SqueaksFormGroupInput): void {
    const squeaksRawValue = this.convertSqueaksToSqueaksRawValue({ ...this.getFormDefaults(), ...squeaks });
    form.reset(
      {
        ...squeaksRawValue,
        id: { value: squeaksRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): SqueaksFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdAt: currentTime,
    };
  }

  private convertSqueaksRawValueToSqueaks(rawSqueaks: SqueaksFormRawValue | NewSqueaksFormRawValue): ISqueaks | NewSqueaks {
    return {
      ...rawSqueaks,
      createdAt: dayjs(rawSqueaks.createdAt, DATE_TIME_FORMAT),
    };
  }

  private convertSqueaksToSqueaksRawValue(
    squeaks: ISqueaks | (Partial<NewSqueaks> & SqueaksFormDefaults)
  ): SqueaksFormRawValue | PartialWithRequiredKeyOf<NewSqueaksFormRawValue> {
    return {
      ...squeaks,
      createdAt: squeaks.createdAt ? squeaks.createdAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
