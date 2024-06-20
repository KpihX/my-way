import { IUser } from 'app/shared/model/user.model';

export interface IItinary {
  id?: number;
  name?: string;
  prefName?: string | null;
  description?: string | null;
  totalDistance?: number;
  totalTime?: number;
  image?: string | null;
  owner?: IUser | null;
}

export const defaultValue: Readonly<IItinary> = {};
