import { ILocation } from 'app/shared/model/location.model';

export interface IItinary {
  id?: number;
  name?: string;
  description?: string | null;
  distance?: number | null;
  duration?: number | null;
  polyline?: string;
  locations?: ILocation[] | null;
}

export const defaultValue: Readonly<IItinary> = {};
