import { ISqueaks } from 'app/entities/squeaks/squeaks.model';

export interface IMentions {
  id: number;
  handle?: string | null;
  squeaks?: Pick<ISqueaks, 'id'> | null;
}

export type NewMentions = Omit<IMentions, 'id'> & { id: null };
