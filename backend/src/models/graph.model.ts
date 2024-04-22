export interface Graph {
	nodes: Node[];
	adjacencyList: { [key: string]: string[] };
}
  
export interface Node {
    id: string;
    label: string;
    // Autres propriétés du nœud
}
  
export interface Edge {
    source: string;
    target: string;
    weight: number;
    // Autres propriétés de l'arête
}