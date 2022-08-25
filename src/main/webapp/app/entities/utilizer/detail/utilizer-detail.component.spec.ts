import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { UtilizerDetailComponent } from './utilizer-detail.component';

describe('Utilizer Management Detail Component', () => {
  let comp: UtilizerDetailComponent;
  let fixture: ComponentFixture<UtilizerDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [UtilizerDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ utilizer: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(UtilizerDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(UtilizerDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load utilizer on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.utilizer).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
