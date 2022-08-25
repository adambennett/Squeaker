import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../utilizer.test-samples';

import { UtilizerFormService } from './utilizer-form.service';

describe('Utilizer Form Service', () => {
  let service: UtilizerFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UtilizerFormService);
  });

  describe('Service methods', () => {
    describe('createUtilizerFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createUtilizerFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            handle: expect.any(Object),
            followers: expect.any(Object),
            following: expect.any(Object),
            user: expect.any(Object),
          })
        );
      });

      it('passing IUtilizer should create a new form with FormGroup', () => {
        const formGroup = service.createUtilizerFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            handle: expect.any(Object),
            followers: expect.any(Object),
            following: expect.any(Object),
            user: expect.any(Object),
          })
        );
      });
    });

    describe('getUtilizer', () => {
      it('should return NewUtilizer for default Utilizer initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createUtilizerFormGroup(sampleWithNewData);

        const utilizer = service.getUtilizer(formGroup) as any;

        expect(utilizer).toMatchObject(sampleWithNewData);
      });

      it('should return NewUtilizer for empty Utilizer initial value', () => {
        const formGroup = service.createUtilizerFormGroup();

        const utilizer = service.getUtilizer(formGroup) as any;

        expect(utilizer).toMatchObject({});
      });

      it('should return IUtilizer', () => {
        const formGroup = service.createUtilizerFormGroup(sampleWithRequiredData);

        const utilizer = service.getUtilizer(formGroup) as any;

        expect(utilizer).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IUtilizer should not enable id FormControl', () => {
        const formGroup = service.createUtilizerFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewUtilizer should disable id FormControl', () => {
        const formGroup = service.createUtilizerFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
