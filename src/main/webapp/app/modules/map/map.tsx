import React, { useState, useEffect, useRef, FC } from 'react';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import 'leaflet-routing-machine';
import 'leaflet-routing-machine/dist/leaflet-routing-machine.css';
// Importer les styles du plugin Leaflet Search
import 'leaflet-search/dist/leaflet-search.min.css';
import 'leaflet-search';

// Extend the RoutingControlOptions interface from 'leaflet-routing-machine'
/* eslint-disable @typescript-eslint/no-namespace */
declare global {
  namespace L.Routing {
    interface RoutingControlOptions {
      createMarker?: (i: number, waypoint: Waypoint, n: number) => L.Marker;
    }
  }
}
/* eslint-enable @typescript-eslint/no-namespace */

const myCustomIcon = L.icon({
  iconUrl: 'content/images/leaflet/marker-icon.png', // Chemin de l'image de marqueur
  iconRetinaUrl: 'content/images/leaflet/marker-icon-2x.png', // Chemin de l'image de marqueur pour Retina
  shadowUrl: 'content/images/leaflet/marker-shadow.png', // Chemin de l'ombre du marqueur
  iconSize: [25, 41], // La taille de l'icône, en pixels
  iconAnchor: [12, 41], // Le point de l'icône qui correspondra à la position du marqueur
  popupAnchor: [1, -34], // Le point à partir duquel la popup du marqueur s'ouvrira
  tooltipAnchor: [16, -28], // Le point à partir duquel l'infobulle du marqueur s'ouvrira
});

const yaounde: L.LatLngExpression = [3.848, 11.502];

const Map: FC = () => {
  const mapContainerRef = useRef<HTMLDivElement>(null);
  const [map, setMap] = useState<L.Map | null>(null);
  const [waypoints, setWaypoints] = useState<L.LatLng[]>([]);
  const [control, setControl] = useState<L.Routing.Control | null>(null);
  const max_number_of_waypoints = 5;

  useEffect(() => {
    if (!mapContainerRef.current) return;
    const initialMap = L.map(mapContainerRef.current).setView(yaounde, 13);
    setMap(initialMap);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 19,
      attribution: '© OpenStreetMap contributors',
    }).addTo(initialMap);

    const routingControl = L.Routing.control({
      router: L.Routing.osrmv1({
        serviceUrl: 'https://router.project-osrm.org/route/v1',
      }),
      routeWhileDragging: true,
      createMarker(i, waypoint, n) {
        const marker = L.marker(waypoint.latLng, {
          icon: myCustomIcon, // Utilisez l'icône personnalisée pour chaque marqueur
        });
        // Ajouter ici d'autres personnalisations de marqueur si nécessaire
        return marker;
      },
    }).addTo(initialMap);
    setControl(routingControl);

    initialMap.locate({ setView: true, maxZoom: 16 });

    initialMap.on('click', e => {
      if (waypoints.length < max_number_of_waypoints) {
        const newWaypoint = L.Routing.waypoint(e.latlng);
        setWaypoints(prevWaypoints => [...prevWaypoints, e.latlng]);
        routingControl.spliceWaypoints(routingControl.getWaypoints().length, 0, newWaypoint);
      }
    });

    initialMap.on('locationfound', e => {
      if (waypoints.length === 0) {
        const redMarker = L.circleMarker(e.latlng, {
          radius: 8,
          fillColor: '#ff0000',
          color: '#000',
          weight: 1,
          opacity: 1,
          fillOpacity: 0.8,
        }).addTo(initialMap);

        setWaypoints(prevWaypoints => [...prevWaypoints, e.latlng]);
        routingControl.spliceWaypoints(0, 0, L.Routing.waypoint(redMarker.getLatLng()));
        initialMap.setView(e.latlng, 13);
      }
    });

    initialMap.on('locationerror', () => {
      alert("Veuillez autoriser l'acces à votre localisation! Cela permettra de personnaliser votre expérience sur notre site.");
      if (waypoints.length === 0) {
        initialMap.setView(yaounde, 13);
      }
    });

    return () => {
      initialMap.remove();
    };
  }, []);

  const calculateRoute = () => {
    if (waypoints.length > 1 && control) {
      const waypointsForRouting = waypoints.map(wp => L.Routing.waypoint(wp));
      control.setWaypoints(waypointsForRouting);
      control.route();
    } else {
      alert('Veuillez sélectionner au moins deux points sur la carte.');
    }
  };

  const showSteps = () => {
    if (waypoints.length > 1) {
      const steps = waypoints
        .slice(1, -1)
        .map((wp, i) => `Étape ${i + 1}: ${wp.toString()}`)
        .join('\n');
      const stepsContainer = document.getElementById('stepsContainer');
      if (stepsContainer) {
        stepsContainer.innerHTML = `<h3>Étapes à suivre :</h3><p>${steps}</p>`;
        stepsContainer.style.display = 'block';
      }
    } else {
      alert('Veuillez sélectionner au moins deux points sur la carte.');
    }
  };

  const goToWaypoints = () => {
    if (waypoints.length > 0 && map) {
      const bounds = L.latLngBounds(waypoints);
      map.fitBounds(bounds.pad(0.1));
    } else {
      alert('Veuillez sélectionner au moins un point sur la carte.');
    }
  };

  return (
    <div>
      <div ref={mapContainerRef} style={{ height: '400px' }} />
      <button onClick={calculateRoute}>Meilleur chemin</button>
      <button onClick={showSteps}>Étapes</button>
      <button onClick={goToWaypoints}>Go!</button>
      <div
        id="stepsContainer"
        style={{
          position: 'absolute',
          top: '10px',
          left: '10px',
          backgroundColor: 'rgba(255, 255, 255, 0.8)',
          padding: '10px',
          display: 'none',
        }}
      />
    </div>
  );
};

export default Map;

// import React from 'react';
// import L from 'leaflet';
// import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet';
// import 'leaflet/dist/leaflet.css';
// import 'bootstrap/dist/css/bootstrap.min.css';

// // Créez une nouvelle icône personnalisée
// const myCustomIcon = L.icon({
//   iconUrl: 'content/images/leaflet/marker-icon.png', // Chemin de l'image de marqueur
//   iconRetinaUrl: 'content/images/leaflet/marker-icon-2x.png', // Chemin de l'image de marqueur pour Retina
//   shadowUrl: 'content/images/leaflet/marker-shadow.png', // Chemin de l'ombre du marqueur
//   iconSize: [25, 41], // La taille de l'icône, en pixels
//   iconAnchor: [12, 41], // Le point de l'icône qui correspondra à la position du marqueur
//   popupAnchor: [1, -34], // Le point à partir duquel la popup du marqueur s'ouvrira
//   tooltipAnchor: [16, -28] // Le point à partir duquel l'infobulle du marqueur s'ouvrira
// });

// const yaounde: [number, number] = [3.848, 11.502]; // Tuple avec deux éléments numériques

// const Map: React.FC = () => {
//   // Définir la position et le zoom par défaut
//   const defaultPosition = yaounde; // Remplacez par la position par défaut souhaitée
//   const defaultZoom = 13; // Remplacez par le niveau de zoom par défaut souhaité

//   return (
//     <MapContainer center={defaultPosition} zoom={defaultZoom} style={{ height: '400px', width: '100%' }}>
//       <TileLayer
//         url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
//         attribution='© <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
//       />
//       <Marker position={defaultPosition} icon={myCustomIcon}>
//         <Popup>
//           Yaoundé.
//         </Popup>
//       </Marker>
//     </MapContainer>
//   );
// };

// export default Map;
