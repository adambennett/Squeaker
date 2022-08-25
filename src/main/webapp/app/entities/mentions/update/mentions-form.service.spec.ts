import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../mentions.test-samples';

import { MentionsFormService } from './mentions-form.service';

describe('Mentions Form Service', () => {
  let service: MentionsFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MentionsFormService);
  });

  describe('Service methods', () => {
    describe('createMentionsFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMentionsFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            handle: expect.any(Object),
            squeaks: expect.any(Object),
          })
        );
      });

      it('passing IMentions should create a new form with FormGroup', () => {
        const formGroup = service.createMentionsFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            handle: expect.any(Object),
            squeaks: expect.any(Object),
          })
        );
      });
    });

    describe('getMentions', () => {
      it('should return NewMentions for default Mentions initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createMentionsFormGroup(sampleWithNewData);

        const mentions = service.getMentions(formGroup) as any;

        expect(mentions).toMatchObject(sampleWithNewData);
      });

      it('should return NewMentions for empty Mentions initial value', () => {
        const formGroup = service.createMentionsFormGroup();

        const mentions = service.getMentions(formGroup) as any;

        expect(mentions).toMatchObject({});
      });

      it('should return IMentions', () => {
        const formGroup = service.createMentionsFormGroup(sampleWithRequiredData);

        const mentions = service.getMentions(formGroup) as any;

        expect(mentions).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMentions should not enable id FormControl', () => {
        const formGroup = service.createMentionsFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMentions should disable id FormControl', () => {
        const formGroup = service.createMentionsFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
