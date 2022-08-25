import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../squeaks.test-samples';

import { SqueaksFormService } from './squeaks-form.service';

describe('Squeaks Form Service', () => {
  let service: SqueaksFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SqueaksFormService);
  });

  describe('Service methods', () => {
    describe('createSqueaksFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSqueaksFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            content: expect.any(Object),
            createdAt: expect.any(Object),
            likes: expect.any(Object),
            image: expect.any(Object),
            video: expect.any(Object),
            utilizer: expect.any(Object),
          })
        );
      });

      it('passing ISqueaks should create a new form with FormGroup', () => {
        const formGroup = service.createSqueaksFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            content: expect.any(Object),
            createdAt: expect.any(Object),
            likes: expect.any(Object),
            image: expect.any(Object),
            video: expect.any(Object),
            utilizer: expect.any(Object),
          })
        );
      });
    });

    describe('getSqueaks', () => {
      it('should return NewSqueaks for default Squeaks initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createSqueaksFormGroup(sampleWithNewData);

        const squeaks = service.getSqueaks(formGroup) as any;

        expect(squeaks).toMatchObject(sampleWithNewData);
      });

      it('should return NewSqueaks for empty Squeaks initial value', () => {
        const formGroup = service.createSqueaksFormGroup();

        const squeaks = service.getSqueaks(formGroup) as any;

        expect(squeaks).toMatchObject({});
      });

      it('should return ISqueaks', () => {
        const formGroup = service.createSqueaksFormGroup(sampleWithRequiredData);

        const squeaks = service.getSqueaks(formGroup) as any;

        expect(squeaks).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISqueaks should not enable id FormControl', () => {
        const formGroup = service.createSqueaksFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSqueaks should disable id FormControl', () => {
        const formGroup = service.createSqueaksFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
