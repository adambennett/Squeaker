import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SqueaksFormService } from './squeaks-form.service';
import { SqueaksService } from '../service/squeaks.service';
import { ISqueaks } from '../squeaks.model';
import { IUtilizer } from 'app/entities/utilizer/utilizer.model';
import { UtilizerService } from 'app/entities/utilizer/service/utilizer.service';

import { SqueaksUpdateComponent } from './squeaks-update.component';

describe('Squeaks Management Update Component', () => {
  let comp: SqueaksUpdateComponent;
  let fixture: ComponentFixture<SqueaksUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let squeaksFormService: SqueaksFormService;
  let squeaksService: SqueaksService;
  let utilizerService: UtilizerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SqueaksUpdateComponent],
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
      .overrideTemplate(SqueaksUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SqueaksUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    squeaksFormService = TestBed.inject(SqueaksFormService);
    squeaksService = TestBed.inject(SqueaksService);
    utilizerService = TestBed.inject(UtilizerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Utilizer query and add missing value', () => {
      const squeaks: ISqueaks = { id: 456 };
      const utilizer: IUtilizer = { id: 88167 };
      squeaks.utilizer = utilizer;

      const utilizerCollection: IUtilizer[] = [{ id: 97645 }];
      jest.spyOn(utilizerService, 'query').mockReturnValue(of(new HttpResponse({ body: utilizerCollection })));
      const additionalUtilizers = [utilizer];
      const expectedCollection: IUtilizer[] = [...additionalUtilizers, ...utilizerCollection];
      jest.spyOn(utilizerService, 'addUtilizerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ squeaks });
      comp.ngOnInit();

      expect(utilizerService.query).toHaveBeenCalled();
      expect(utilizerService.addUtilizerToCollectionIfMissing).toHaveBeenCalledWith(
        utilizerCollection,
        ...additionalUtilizers.map(expect.objectContaining)
      );
      expect(comp.utilizersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const squeaks: ISqueaks = { id: 456 };
      const utilizer: IUtilizer = { id: 37991 };
      squeaks.utilizer = utilizer;

      activatedRoute.data = of({ squeaks });
      comp.ngOnInit();

      expect(comp.utilizersSharedCollection).toContain(utilizer);
      expect(comp.squeaks).toEqual(squeaks);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISqueaks>>();
      const squeaks = { id: 123 };
      jest.spyOn(squeaksFormService, 'getSqueaks').mockReturnValue(squeaks);
      jest.spyOn(squeaksService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ squeaks });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: squeaks }));
      saveSubject.complete();

      // THEN
      expect(squeaksFormService.getSqueaks).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(squeaksService.update).toHaveBeenCalledWith(expect.objectContaining(squeaks));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISqueaks>>();
      const squeaks = { id: 123 };
      jest.spyOn(squeaksFormService, 'getSqueaks').mockReturnValue({ id: null });
      jest.spyOn(squeaksService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ squeaks: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: squeaks }));
      saveSubject.complete();

      // THEN
      expect(squeaksFormService.getSqueaks).toHaveBeenCalled();
      expect(squeaksService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISqueaks>>();
      const squeaks = { id: 123 };
      jest.spyOn(squeaksService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ squeaks });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(squeaksService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUtilizer', () => {
      it('Should forward to utilizerService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(utilizerService, 'compareUtilizer');
        comp.compareUtilizer(entity, entity2);
        expect(utilizerService.compareUtilizer).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
