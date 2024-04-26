import { Itinerary, ItineraryStep } from '../interfaces/Itinerary';
import OSMService from './OSMService';
import DijkstraService from './DijkstraService';

class ItineraryService {
  async generateItinerary(start: string, end: string): Promise<Itinerary> {
    const osmService = new OSMService();
    const dijkstraService = new DijkstraService();

    // Get OSM data for start and end nodes
    const startNode = await osmService.getOSMNode(start);
    const endNode = await osmService.getOSMNode(end);

    // Build graph for Dijkstra's algorithm
    // ...

    // Run Dijkstra's algorithm
    const dijkstraResult = dijkstraService.dijkstra({
      start,
      end,
      graph: {},
    });

    // Build itinerary steps
    const steps: ItineraryStep[] = [];
    // ...

    return {
      steps,
      totalDistance: 0,
      totalDuration: 0,
    };
  }
}

export default new ItineraryService();