import L from 'leaflet';

export const marquerIcon = L.icon({
  iconUrl: 'content/images/leaflet/marker-icon.png', // Chemin de l'image de marqueur
  iconRetinaUrl: 'content/images/leaflet/marker-icon-2x.png', // Chemin de l'image de marqueur pour Retina
  shadowUrl: 'content/images/leaflet/marker-shadow.png', // Chemin de l'ombre du marqueur
  iconSize: [25, 41], // La taille de l'icône, en pixels
  iconAnchor: [12, 41], // Le point de l'icône qui correspondra à la position du marqueur
  popupAnchor: [1, -34], // Le point à partir duquel la popup du marqueur s'ouvrira
  tooltipAnchor: [16, -28], // Le point à partir duquel l'infobulle du marqueur s'ouvrira
});

export const middleMarquer = L.divIcon({
  className: 'custom-marker-icon',
  html: `<div style="width: 18px; height: 18px; border: 2px solid white; background-color: gray; border-radius: 50%;"></div>`,
  iconAnchor: [9, 9], // Centre du cercle
  popupAnchor: [0, -9], // Popup centrée au-dessus du cercle
});

export const endMarquer = L.divIcon({
  className: 'custom-marker-icon',
  html: `<div style="width: 18px; height: 18px; border: 2px solid white; background-color: green; border-radius: 50%;"></div>`,
  iconAnchor: [9, 9], // Centre du cercle
  popupAnchor: [0, -9], // Popup centrée au-dessus du cercle
});

export const positionMarquer = L.divIcon({
  className: 'custom-marker-icon',
  html: `<div style="width: 18px; height: 18px; border: 2px solid white; background-color: blue; border-radius: 50%;"></div>`,
  iconAnchor: [9, 9], // Centre du cercle
  popupAnchor: [0, -9], // Popup centrée au-dessus du cercle
});
