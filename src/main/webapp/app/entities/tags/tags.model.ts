import { ISqueaks } from 'app/entities/squeaks/squeaks.model';

export interface ITags {
  id: number;
  hashtag?: string | null;
  squeaks?: Pick<ISqueaks, 'id'> | null;
}

export type NewTags = Omit<ITags, 'id'> & { id: null };
