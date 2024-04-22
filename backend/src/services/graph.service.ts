import { Graph, Node, Edge } from '../models/graph.model';

class GraphService {
  private graph: Graph;

  constructor() {
    this.graph = {
      nodes: [],
      adjacencyList: {},
    };
  }

  addNode(node: Node): void {
    this.graph.nodes.push(node);
    this.graph.adjacencyList[node.id] = [];
  }

  addEdge(edge: Edge): void {
    this.graph.adjacencyList[edge.source].push(edge.target);
  }

  getNeighbors(nodeId: string): string[] {
    return this.graph.adjacencyList[nodeId];
  }

  // Autres fonctions de manipulation du graphe
}

export default GraphService;