export interface OSMNode {
    id: number;
    lat: number;
    lon: number;
  }
  
  export interface OSMWay {
    id: number;
    nodes: OSMNode[];
  }
  
  export interface OSMTags {
    [key: string]: string;
  }
  
  export interface OSMElement {
    type: 'node' | 'way';
    id: number;
    tags: OSMTags;
  }