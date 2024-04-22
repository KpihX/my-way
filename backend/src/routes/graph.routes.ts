import { Router } from 'express';
import GraphController from '../controllers/graph.controllers';

const router = Router();
const graphController = new GraphController();

router.post('/nodes', graphController.addNode);
router.post('/edges', graphController.addEdge);
router.get('/nodes/:nodeId/neighbors', graphController.getNeighbors);

// Autres routes du graphe

export default router;