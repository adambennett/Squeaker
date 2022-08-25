import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMentions, NewMentions } from '../mentions.model';

export type PartialUpdateMentions = Partial<IMentions> & Pick<IMentions, 'id'>;

export type EntityResponseType = HttpResponse<IMentions>;
export type EntityArrayResponseType = HttpResponse<IMentions[]>;

@Injectable({ providedIn: 'root' })
export class MentionsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/mentions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(mentions: NewMentions): Observable<EntityResponseType> {
    return this.http.post<IMentions>(this.resourceUrl, mentions, { observe: 'response' });
  }

  update(mentions: IMentions): Observable<EntityResponseType> {
    return this.http.put<IMentions>(`${this.resourceUrl}/${this.getMentionsIdentifier(mentions)}`, mentions, { observe: 'response' });
  }

  partialUpdate(mentions: PartialUpdateMentions): Observable<EntityResponseType> {
    return this.http.patch<IMentions>(`${this.resourceUrl}/${this.getMentionsIdentifier(mentions)}`, mentions, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMentions>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMentions[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMentionsIdentifier(mentions: Pick<IMentions, 'id'>): number {
    return mentions.id;
  }

  compareMentions(o1: Pick<IMentions, 'id'> | null, o2: Pick<IMentions, 'id'> | null): boolean {
    return o1 && o2 ? this.getMentionsIdentifier(o1) === this.getMentionsIdentifier(o2) : o1 === o2;
  }

  addMentionsToCollectionIfMissing<Type extends Pick<IMentions, 'id'>>(
    mentionsCollection: Type[],
    ...mentionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const mentions: Type[] = mentionsToCheck.filter(isPresent);
    if (mentions.length > 0) {
      const mentionsCollectionIdentifiers = mentionsCollection.map(mentionsItem => this.getMentionsIdentifier(mentionsItem)!);
      const mentionsToAdd = mentions.filter(mentionsItem => {
        const mentionsIdentifier = this.getMentionsIdentifier(mentionsItem);
        if (mentionsCollectionIdentifiers.includes(mentionsIdentifier)) {
          return false;
        }
        mentionsCollectionIdentifiers.push(mentionsIdentifier);
        return true;
      });
      return [...mentionsToAdd, ...mentionsCollection];
    }
    return mentionsCollection;
  }
}
