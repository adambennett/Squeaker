import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IUtilizer } from '../utilizer.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../utilizer.test-samples';

import { UtilizerService } from './utilizer.service';

const requireRestSample: IUtilizer = {
  ...sampleWithRequiredData,
};

describe('Utilizer Service', () => {
  let service: UtilizerService;
  let httpMock: HttpTestingController;
  let expectedResult: IUtilizer | IUtilizer[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(UtilizerService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Utilizer', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const utilizer = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(utilizer).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Utilizer', () => {
      const utilizer = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(utilizer).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Utilizer', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Utilizer', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Utilizer', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addUtilizerToCollectionIfMissing', () => {
      it('should add a Utilizer to an empty array', () => {
        const utilizer: IUtilizer = sampleWithRequiredData;
        expectedResult = service.addUtilizerToCollectionIfMissing([], utilizer);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(utilizer);
      });

      it('should not add a Utilizer to an array that contains it', () => {
        const utilizer: IUtilizer = sampleWithRequiredData;
        const utilizerCollection: IUtilizer[] = [
          {
            ...utilizer,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addUtilizerToCollectionIfMissing(utilizerCollection, utilizer);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Utilizer to an array that doesn't contain it", () => {
        const utilizer: IUtilizer = sampleWithRequiredData;
        const utilizerCollection: IUtilizer[] = [sampleWithPartialData];
        expectedResult = service.addUtilizerToCollectionIfMissing(utilizerCollection, utilizer);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(utilizer);
      });

      it('should add only unique Utilizer to an array', () => {
        const utilizerArray: IUtilizer[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const utilizerCollection: IUtilizer[] = [sampleWithRequiredData];
        expectedResult = service.addUtilizerToCollectionIfMissing(utilizerCollection, ...utilizerArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const utilizer: IUtilizer = sampleWithRequiredData;
        const utilizer2: IUtilizer = sampleWithPartialData;
        expectedResult = service.addUtilizerToCollectionIfMissing([], utilizer, utilizer2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(utilizer);
        expect(expectedResult).toContain(utilizer2);
      });

      it('should accept null and undefined values', () => {
        const utilizer: IUtilizer = sampleWithRequiredData;
        expectedResult = service.addUtilizerToCollectionIfMissing([], null, utilizer, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(utilizer);
      });

      it('should return initial array if no Utilizer is added', () => {
        const utilizerCollection: IUtilizer[] = [sampleWithRequiredData];
        expectedResult = service.addUtilizerToCollectionIfMissing(utilizerCollection, undefined, null);
        expect(expectedResult).toEqual(utilizerCollection);
      });
    });

    describe('compareUtilizer', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareUtilizer(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareUtilizer(entity1, entity2);
        const compareResult2 = service.compareUtilizer(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareUtilizer(entity1, entity2);
        const compareResult2 = service.compareUtilizer(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareUtilizer(entity1, entity2);
        const compareResult2 = service.compareUtilizer(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
