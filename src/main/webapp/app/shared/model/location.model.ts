import { ILocationCategory } from 'app/shared/model/location-category.model';
import { IItinary } from 'app/shared/model/itinary.model';

export interface ILocation {
  id?: number;
  latitude?: number;
  longitude?: number;
  name?: string;
  description?: string | null;
  address?: string | null;
  category?: ILocationCategory | null;
  itinaries?: IItinary[] | null;
}

export const defaultValue: Readonly<ILocation> = {};
