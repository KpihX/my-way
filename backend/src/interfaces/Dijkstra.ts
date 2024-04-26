export interface DijkstraOptions {
    start: string;
    end: string;
    graph: {
      nodes: {
        [key: string]: number;
      };
      edges: {
        [key: string]: number;
      };
    };
  }
  
export interface DijkstraResult {
    shortestDistance: number;
    shortestPath: string[];
  }