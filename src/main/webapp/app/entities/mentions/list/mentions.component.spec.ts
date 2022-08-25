import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { MentionsService } from '../service/mentions.service';

import { MentionsComponent } from './mentions.component';

describe('Mentions Management Component', () => {
  let comp: MentionsComponent;
  let fixture: ComponentFixture<MentionsComponent>;
  let service: MentionsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'mentions', component: MentionsComponent }]), HttpClientTestingModule],
      declarations: [MentionsComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(MentionsComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MentionsComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(MentionsService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.mentions?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to mentionsService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getMentionsIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getMentionsIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
