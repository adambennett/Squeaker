import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISqueaks, NewSqueaks } from '../squeaks.model';

export type PartialUpdateSqueaks = Partial<ISqueaks> & Pick<ISqueaks, 'id'>;

type RestOf<T extends ISqueaks | NewSqueaks> = Omit<T, 'createdAt'> & {
  createdAt?: string | null;
};

export type RestSqueaks = RestOf<ISqueaks>;

export type NewRestSqueaks = RestOf<NewSqueaks>;

export type PartialUpdateRestSqueaks = RestOf<PartialUpdateSqueaks>;

export type EntityResponseType = HttpResponse<ISqueaks>;
export type EntityArrayResponseType = HttpResponse<ISqueaks[]>;

@Injectable({ providedIn: 'root' })
export class SqueaksService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/squeaks');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(squeaks: NewSqueaks): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(squeaks);
    return this.http
      .post<RestSqueaks>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(squeaks: ISqueaks): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(squeaks);
    return this.http
      .put<RestSqueaks>(`${this.resourceUrl}/${this.getSqueaksIdentifier(squeaks)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(squeaks: PartialUpdateSqueaks): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(squeaks);
    return this.http
      .patch<RestSqueaks>(`${this.resourceUrl}/${this.getSqueaksIdentifier(squeaks)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestSqueaks>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestSqueaks[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSqueaksIdentifier(squeaks: Pick<ISqueaks, 'id'>): number {
    return squeaks.id;
  }

  compareSqueaks(o1: Pick<ISqueaks, 'id'> | null, o2: Pick<ISqueaks, 'id'> | null): boolean {
    return o1 && o2 ? this.getSqueaksIdentifier(o1) === this.getSqueaksIdentifier(o2) : o1 === o2;
  }

  addSqueaksToCollectionIfMissing<Type extends Pick<ISqueaks, 'id'>>(
    squeaksCollection: Type[],
    ...squeaksToCheck: (Type | null | undefined)[]
  ): Type[] {
    const squeaks: Type[] = squeaksToCheck.filter(isPresent);
    if (squeaks.length > 0) {
      const squeaksCollectionIdentifiers = squeaksCollection.map(squeaksItem => this.getSqueaksIdentifier(squeaksItem)!);
      const squeaksToAdd = squeaks.filter(squeaksItem => {
        const squeaksIdentifier = this.getSqueaksIdentifier(squeaksItem);
        if (squeaksCollectionIdentifiers.includes(squeaksIdentifier)) {
          return false;
        }
        squeaksCollectionIdentifiers.push(squeaksIdentifier);
        return true;
      });
      return [...squeaksToAdd, ...squeaksCollection];
    }
    return squeaksCollection;
  }

  protected convertDateFromClient<T extends ISqueaks | NewSqueaks | PartialUpdateSqueaks>(squeaks: T): RestOf<T> {
    return {
      ...squeaks,
      createdAt: squeaks.createdAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restSqueaks: RestSqueaks): ISqueaks {
    return {
      ...restSqueaks,
      createdAt: restSqueaks.createdAt ? dayjs(restSqueaks.createdAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestSqueaks>): HttpResponse<ISqueaks> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestSqueaks[]>): HttpResponse<ISqueaks[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
