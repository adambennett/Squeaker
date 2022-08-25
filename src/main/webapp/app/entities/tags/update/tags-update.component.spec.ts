import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TagsFormService } from './tags-form.service';
import { TagsService } from '../service/tags.service';
import { ITags } from '../tags.model';
import { ISqueaks } from 'app/entities/squeaks/squeaks.model';
import { SqueaksService } from 'app/entities/squeaks/service/squeaks.service';

import { TagsUpdateComponent } from './tags-update.component';

describe('Tags Management Update Component', () => {
  let comp: TagsUpdateComponent;
  let fixture: ComponentFixture<TagsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let tagsFormService: TagsFormService;
  let tagsService: TagsService;
  let squeaksService: SqueaksService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TagsUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(TagsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TagsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tagsFormService = TestBed.inject(TagsFormService);
    tagsService = TestBed.inject(TagsService);
    squeaksService = TestBed.inject(SqueaksService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Squeaks query and add missing value', () => {
      const tags: ITags = { id: 456 };
      const squeaks: ISqueaks = { id: 99040 };
      tags.squeaks = squeaks;

      const squeaksCollection: ISqueaks[] = [{ id: 33199 }];
      jest.spyOn(squeaksService, 'query').mockReturnValue(of(new HttpResponse({ body: squeaksCollection })));
      const additionalSqueaks = [squeaks];
      const expectedCollection: ISqueaks[] = [...additionalSqueaks, ...squeaksCollection];
      jest.spyOn(squeaksService, 'addSqueaksToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ tags });
      comp.ngOnInit();

      expect(squeaksService.query).toHaveBeenCalled();
      expect(squeaksService.addSqueaksToCollectionIfMissing).toHaveBeenCalledWith(
        squeaksCollection,
        ...additionalSqueaks.map(expect.objectContaining)
      );
      expect(comp.squeaksSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const tags: ITags = { id: 456 };
      const squeaks: ISqueaks = { id: 55387 };
      tags.squeaks = squeaks;

      activatedRoute.data = of({ tags });
      comp.ngOnInit();

      expect(comp.squeaksSharedCollection).toContain(squeaks);
      expect(comp.tags).toEqual(tags);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITags>>();
      const tags = { id: 123 };
      jest.spyOn(tagsFormService, 'getTags').mockReturnValue(tags);
      jest.spyOn(tagsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tags });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tags }));
      saveSubject.complete();

      // THEN
      expect(tagsFormService.getTags).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(tagsService.update).toHaveBeenCalledWith(expect.objectContaining(tags));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITags>>();
      const tags = { id: 123 };
      jest.spyOn(tagsFormService, 'getTags').mockReturnValue({ id: null });
      jest.spyOn(tagsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tags: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tags }));
      saveSubject.complete();

      // THEN
      expect(tagsFormService.getTags).toHaveBeenCalled();
      expect(tagsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITags>>();
      const tags = { id: 123 };
      jest.spyOn(tagsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tags });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tagsService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareSqueaks', () => {
      it('Should forward to squeaksService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(squeaksService, 'compareSqueaks');
        comp.compareSqueaks(entity, entity2);
        expect(squeaksService.compareSqueaks).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
