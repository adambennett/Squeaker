import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUtilizer, NewUtilizer } from '../utilizer.model';

export type PartialUpdateUtilizer = Partial<IUtilizer> & Pick<IUtilizer, 'id'>;

export type EntityResponseType = HttpResponse<IUtilizer>;
export type EntityArrayResponseType = HttpResponse<IUtilizer[]>;

@Injectable({ providedIn: 'root' })
export class UtilizerService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/utilizers');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(utilizer: NewUtilizer): Observable<EntityResponseType> {
    return this.http.post<IUtilizer>(this.resourceUrl, utilizer, { observe: 'response' });
  }

  update(utilizer: IUtilizer): Observable<EntityResponseType> {
    return this.http.put<IUtilizer>(`${this.resourceUrl}/${this.getUtilizerIdentifier(utilizer)}`, utilizer, { observe: 'response' });
  }

  partialUpdate(utilizer: PartialUpdateUtilizer): Observable<EntityResponseType> {
    return this.http.patch<IUtilizer>(`${this.resourceUrl}/${this.getUtilizerIdentifier(utilizer)}`, utilizer, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUtilizer>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUtilizer[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getUtilizerIdentifier(utilizer: Pick<IUtilizer, 'id'>): number {
    return utilizer.id;
  }

  compareUtilizer(o1: Pick<IUtilizer, 'id'> | null, o2: Pick<IUtilizer, 'id'> | null): boolean {
    return o1 && o2 ? this.getUtilizerIdentifier(o1) === this.getUtilizerIdentifier(o2) : o1 === o2;
  }

  addUtilizerToCollectionIfMissing<Type extends Pick<IUtilizer, 'id'>>(
    utilizerCollection: Type[],
    ...utilizersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const utilizers: Type[] = utilizersToCheck.filter(isPresent);
    if (utilizers.length > 0) {
      const utilizerCollectionIdentifiers = utilizerCollection.map(utilizerItem => this.getUtilizerIdentifier(utilizerItem)!);
      const utilizersToAdd = utilizers.filter(utilizerItem => {
        const utilizerIdentifier = this.getUtilizerIdentifier(utilizerItem);
        if (utilizerCollectionIdentifiers.includes(utilizerIdentifier)) {
          return false;
        }
        utilizerCollectionIdentifiers.push(utilizerIdentifier);
        return true;
      });
      return [...utilizersToAdd, ...utilizerCollection];
    }
    return utilizerCollection;
  }
}
