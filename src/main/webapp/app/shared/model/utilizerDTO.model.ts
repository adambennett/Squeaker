import { IUser } from './user.model';

export interface UtilizerDTO {
  id: number;
  handle: string;
  followers: number;
  following: number;
  user: IUser;
}
