import { IUser } from 'app/shared/model/user.model';
import { IPlaceCategory } from 'app/shared/model/place-category.model';
import { IItinary } from 'app/shared/model/itinary.model';

export interface IPlace {
  id?: number;
  name?: string;
  displayName?: string | null;
  lat?: number;
  lon?: number;
  image?: string | null;
  owner?: IUser | null;
  category?: IPlaceCategory | null;
  itinary?: IItinary | null;
}

export const defaultValue: Readonly<IPlace> = {};
