import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MentionsFormService } from './mentions-form.service';
import { MentionsService } from '../service/mentions.service';
import { IMentions } from '../mentions.model';
import { ISqueaks } from 'app/entities/squeaks/squeaks.model';
import { SqueaksService } from 'app/entities/squeaks/service/squeaks.service';

import { MentionsUpdateComponent } from './mentions-update.component';

describe('Mentions Management Update Component', () => {
  let comp: MentionsUpdateComponent;
  let fixture: ComponentFixture<MentionsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let mentionsFormService: MentionsFormService;
  let mentionsService: MentionsService;
  let squeaksService: SqueaksService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [MentionsUpdateComponent],
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
      .overrideTemplate(MentionsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MentionsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    mentionsFormService = TestBed.inject(MentionsFormService);
    mentionsService = TestBed.inject(MentionsService);
    squeaksService = TestBed.inject(SqueaksService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Squeaks query and add missing value', () => {
      const mentions: IMentions = { id: 456 };
      const squeaks: ISqueaks = { id: 73227 };
      mentions.squeaks = squeaks;

      const squeaksCollection: ISqueaks[] = [{ id: 80188 }];
      jest.spyOn(squeaksService, 'query').mockReturnValue(of(new HttpResponse({ body: squeaksCollection })));
      const additionalSqueaks = [squeaks];
      const expectedCollection: ISqueaks[] = [...additionalSqueaks, ...squeaksCollection];
      jest.spyOn(squeaksService, 'addSqueaksToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ mentions });
      comp.ngOnInit();

      expect(squeaksService.query).toHaveBeenCalled();
      expect(squeaksService.addSqueaksToCollectionIfMissing).toHaveBeenCalledWith(
        squeaksCollection,
        ...additionalSqueaks.map(expect.objectContaining)
      );
      expect(comp.squeaksSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const mentions: IMentions = { id: 456 };
      const squeaks: ISqueaks = { id: 30972 };
      mentions.squeaks = squeaks;

      activatedRoute.data = of({ mentions });
      comp.ngOnInit();

      expect(comp.squeaksSharedCollection).toContain(squeaks);
      expect(comp.mentions).toEqual(mentions);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMentions>>();
      const mentions = { id: 123 };
      jest.spyOn(mentionsFormService, 'getMentions').mockReturnValue(mentions);
      jest.spyOn(mentionsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ mentions });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: mentions }));
      saveSubject.complete();

      // THEN
      expect(mentionsFormService.getMentions).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(mentionsService.update).toHaveBeenCalledWith(expect.objectContaining(mentions));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMentions>>();
      const mentions = { id: 123 };
      jest.spyOn(mentionsFormService, 'getMentions').mockReturnValue({ id: null });
      jest.spyOn(mentionsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ mentions: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: mentions }));
      saveSubject.complete();

      // THEN
      expect(mentionsFormService.getMentions).toHaveBeenCalled();
      expect(mentionsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMentions>>();
      const mentions = { id: 123 };
      jest.spyOn(mentionsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ mentions });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(mentionsService.update).toHaveBeenCalled();
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
