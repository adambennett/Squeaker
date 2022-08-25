import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMentions } from '../mentions.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../mentions.test-samples';

import { MentionsService } from './mentions.service';

const requireRestSample: IMentions = {
  ...sampleWithRequiredData,
};

describe('Mentions Service', () => {
  let service: MentionsService;
  let httpMock: HttpTestingController;
  let expectedResult: IMentions | IMentions[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MentionsService);
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

    it('should create a Mentions', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const mentions = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(mentions).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Mentions', () => {
      const mentions = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(mentions).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Mentions', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Mentions', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Mentions', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addMentionsToCollectionIfMissing', () => {
      it('should add a Mentions to an empty array', () => {
        const mentions: IMentions = sampleWithRequiredData;
        expectedResult = service.addMentionsToCollectionIfMissing([], mentions);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(mentions);
      });

      it('should not add a Mentions to an array that contains it', () => {
        const mentions: IMentions = sampleWithRequiredData;
        const mentionsCollection: IMentions[] = [
          {
            ...mentions,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMentionsToCollectionIfMissing(mentionsCollection, mentions);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Mentions to an array that doesn't contain it", () => {
        const mentions: IMentions = sampleWithRequiredData;
        const mentionsCollection: IMentions[] = [sampleWithPartialData];
        expectedResult = service.addMentionsToCollectionIfMissing(mentionsCollection, mentions);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(mentions);
      });

      it('should add only unique Mentions to an array', () => {
        const mentionsArray: IMentions[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const mentionsCollection: IMentions[] = [sampleWithRequiredData];
        expectedResult = service.addMentionsToCollectionIfMissing(mentionsCollection, ...mentionsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const mentions: IMentions = sampleWithRequiredData;
        const mentions2: IMentions = sampleWithPartialData;
        expectedResult = service.addMentionsToCollectionIfMissing([], mentions, mentions2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(mentions);
        expect(expectedResult).toContain(mentions2);
      });

      it('should accept null and undefined values', () => {
        const mentions: IMentions = sampleWithRequiredData;
        expectedResult = service.addMentionsToCollectionIfMissing([], null, mentions, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(mentions);
      });

      it('should return initial array if no Mentions is added', () => {
        const mentionsCollection: IMentions[] = [sampleWithRequiredData];
        expectedResult = service.addMentionsToCollectionIfMissing(mentionsCollection, undefined, null);
        expect(expectedResult).toEqual(mentionsCollection);
      });
    });

    describe('compareMentions', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMentions(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareMentions(entity1, entity2);
        const compareResult2 = service.compareMentions(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareMentions(entity1, entity2);
        const compareResult2 = service.compareMentions(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareMentions(entity1, entity2);
        const compareResult2 = service.compareMentions(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
