import { DijkstraOptions, DijkstraResult } from '../interfaces/Dijkstra';

class DijkstraService {
  private graph: {
    nodes: {
      [key: string]: number;
    };
    edges: {
      [key: string]: number;
    };
  };

  constructor() {
    this.graph = {
      nodes: {},
      edges: {},
    };
  }

  addNode(nodeId: string, distance: number) {
    this.graph.nodes[nodeId] = distance;
  }

  addEdge(from: string, to: string, distance: number) {
    this.graph.edges[`${from}->${to}`] = distance;
  }

  dijkstra({ start, end, graph }: DijkstraOptions): DijkstraResult {
    const distances: { [key: string]: number } = {};
    const previous: { [key: string]: string | null } = {};
    const queue: { node: string, distance: number }[] = [];
  
    // Initialize distances and previous nodes
    for (const node in graph.nodes) {
      distances[node] = Infinity;
      previous[node] = null;
    }
    distances[start] = 0;
  
    // Add all nodes to the priority queue
    for (const node in graph.nodes) {
      queue.push({ node, distance: distances[node] });
    }
  
    // Use a priority queue to efficiently select the next node
    queue.sort((a, b) => a.distance - b.distance);
  
    while (queue.length > 0) {
      const { node, distance } = queue.shift();
  
      // If we've reached the end node, we're done
      if (node === end) {
        break;
      }
  
      // If the current distance is greater than the known distance, skip
      if (distance > distances[node]) {
        continue;
      }
  
      // Update distances and previous nodes for neighbors
      for (const neighbor in graph.edges) {
        if (neighbor.startsWith(`${node}->`)) {
          const neighborId = neighbor.split('->')[1];
          const newDistance = distance + graph.edges[neighbor];
  
          if (newDistance < distances[neighborId]) {
            distances[neighborId] = newDistance;
            previous[neighborId] = node;
  
            // Update the priority queue
            const index = queue.findIndex((item) => item.node === neighborId);
            if (index !== -1) {
              queue[index].distance = newDistance;
              queue.sort((a, b) => a.distance - b.distance);
            }
          }
        }
      }
    }
  
    // Build the shortest path
    const path: string[] = [];
    let currentNode = end;
    while (currentNode !== null) {
      path.unshift(currentNode);
      currentNode = previous[currentNode];
    }
  
    return {
      shortestDistance: distances[end],
      shortestPath: path,
    };
  }
}

export default new DijkstraService();