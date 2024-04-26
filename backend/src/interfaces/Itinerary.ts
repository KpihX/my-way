export interface ItineraryStep {
    from: string;
    to: string;
    distance: number;
    duration: number;
  }
  
export interface Itinerary {
    steps: ItineraryStep[];
    totalDistance: number;
    totalDuration: number;
  }