import dayjs from 'dayjs/esm';
import { IUtilizer } from 'app/entities/utilizer/utilizer.model';

export interface ISqueaks {
  id: number;
  content?: string | null;
  createdAt?: dayjs.Dayjs | null;
  likes?: number | null;
  image?: string | null;
  imageContentType?: string | null;
  video?: string | null;
  videoContentType?: string | null;
  utilizer?: Pick<IUtilizer, 'id' | 'handle'> | null;
}

export type NewSqueaks = Omit<ISqueaks, 'id'> & { id: null };
