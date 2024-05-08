import { IUser } from 'app/shared/model/user.model';

export interface IFavoriteLocation {
  id?: number;
  name?: string;
  user?: IUser | null;
}

export const defaultValue: Readonly<IFavoriteLocation> = {};
