import axios from 'axios';
import cheerio from 'cheerio';

const OSM_API_URL = 'https://api.openstreetmap.org';

async function getNodeCoordinates(nodeId: string): Promise<{ lat: number, lng: number }> {
  try {
    const response = await axios.get(`${OSM_API_URL}/api/0.6/node/${nodeId}`);
    const $ = cheerio.load(response.data);
    const lat = parseFloat($('[lat]').attr('lat'));
    const lng = parseFloat($('[lon]').attr('lon'));
    return { lat, lng };
  } catch (error) {
    console.error('Error fetching node coordinates from OpenStreetMap:', error);
    throw error;
  }
}

async function searchNodes(query: string): Promise<{ nodeId: string, name: string }[]> {
  try {
    const response = await axios.get(`${OSM_API_URL}/api/0.6/map?bbox=-180,-90,180,90&q=${query}`);
    const $ = cheerio.load(response.data);
    const nodes = $('node').map((index, element) => ({
      nodeId: $(element).attr('id'),
      name: $(element).attr('visible'),
    })).get();
    return nodes;
  } catch (error) {
    console.error('Error searching nodes on OpenStreetMap:', error);
    throw error;
  }
}

export { getNodeCoordinates, searchNodes };