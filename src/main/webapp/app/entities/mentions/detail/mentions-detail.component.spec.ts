import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MentionsDetailComponent } from './mentions-detail.component';

describe('Mentions Management Detail Component', () => {
  let comp: MentionsDetailComponent;
  let fixture: ComponentFixture<MentionsDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MentionsDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ mentions: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(MentionsDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(MentionsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load mentions on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.mentions).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
