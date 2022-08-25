import { IUser } from 'app/entities/user/user.model';

export interface IUtilizer {
  id: number;
  handle?: string | null;
  followers?: number | null;
  following?: number | null;
  user?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewUtilizer = Omit<IUtilizer, 'id'> & { id: null };
