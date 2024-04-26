import axios from 'axios';
import { OSMNode, OSMWay, OSMTags, OSMElement } from '../interfaces/OSM';

const osmApiUrl = '(link unavailable)';

class OSMService {
  async getOSMData(bbox: string): Promise<OSMElement[]> {
    const response = await axios.get(`${osmApiUrl}/map?bbox=${bbox}`);
    return response.data.elements;
  }

  async getOSMNode(id: number): Promise<OSMNode | null> {
    const response = await axios.get(`${osmApiUrl}/node/${id}`);
    return response.data;
  }

  async getOSMWay(id: number): Promise<OSMWay | null> {
    const response = await axios.get(`${osmApiUrl}/way/${id}`);
    return response.data;
  }
}

export default new OSMService();