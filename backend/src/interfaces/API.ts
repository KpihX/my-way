import { Itinerary } from "./Itinerary";

export interface APIResponse<T> {
    data: T;
    error?: string;
  }
  
export interface GetItineraryRequest {
    start: string;
    end: string;
  }
  
export interface GetItineraryResponse extends APIResponse<Itinerary> {}