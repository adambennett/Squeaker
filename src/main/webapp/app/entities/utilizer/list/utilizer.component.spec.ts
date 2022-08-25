import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { UtilizerService } from '../service/utilizer.service';

import { UtilizerComponent } from './utilizer.component';

describe('Utilizer Management Component', () => {
  let comp: UtilizerComponent;
  let fixture: ComponentFixture<UtilizerComponent>;
  let service: UtilizerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'utilizer', component: UtilizerComponent }]), HttpClientTestingModule],
      declarations: [UtilizerComponent],
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
      .overrideTemplate(UtilizerComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UtilizerComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(UtilizerService);

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
    expect(comp.utilizers?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to utilizerService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getUtilizerIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getUtilizerIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
