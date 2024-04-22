import { Request, Response } from 'express';

import GraphService from '../services/graph.service';
import { Node, Edge } from '../models/graph.model'

class GraphController {
  private graphService: GraphService;

  constructor() {
    this.graphService = new GraphService();
  }

  addNode(req: Request, res: Response): void {
    const { id, label } = req.body;
    const node: Node = { id, label };
    this.graphService.addNode(node);
    res.status(200).json({ message: 'Node added successfully' });
  }

  addEdge(req: Request, res: Response): void {
    const { source, target, weight } = req.body;
    const edge: Edge = { source, target, weight };
    this.graphService.addEdge(edge);
    res.status(200).json({ message: 'Edge added successfully' });
  }

  getNeighbors(req: Request, res: Response): void {
    const { nodeId } = req.params;
    const neighbors = this.graphService.getNeighbors(nodeId);
    res.status(200).json({ neighbors });
  }

  // Autres fonctions de contrôle du graphe
}

export default GraphController;