import React, { useState, useEffect, useRef, FC } from 'react';
// s, { Map as LeafletMap } from 'leaflet';
// import 'leaflet/dist/leaflet.css';
// import 'leaflet-routing-machine';
// import 'leaflet-routing-machine/dist/leaflet-routing-machine.css';
// Importer les styles du plugin Leaflet Search
// import 'leaflet-search/dist/leaflet-search.min.css';
// import 'leaflet-search';
import { MapContainer, useMap, TileLayer, useMapEvents, Marker, Popup } from 'react-leaflet';
import LocationButton from './locationButton';
import { marquerIcon } from './icons';
import LocationMarker from './locationMarker';
import RoutingMap from './routingMap';
import { AiOutlineInfoCircle } from 'react-icons/ai';

const yaounde: L.LatLngExpression = [3.848, 11.502];

function MapEvents({ points, setPoints, max_points }) {
  useMapEvents({
    click(e) {
      addPoint(e.latlng);
    },
  });

  const addPoint = (point: L.LatLng) => {
    if (points.length >= max_points) {
      alert('Vous avez atteint le nombre maximum de points.');
      return;
    }
    setPoints([...points, point]);
  };

  return null; // Ce composant ne rend rien lui-même
}

const travelModes = ['driving', 'walking', 'cycling'];

const Map: FC = () => {
  // const mapContainerRef = useRef<HTMLDivElement>(null);
  // const [map, setMap] = useState<L.Map | null>(null);
  // const [waypoints, setWaypoints] = useState<L.LatLng[]>([]);
  // const [control, setControl] = useState<L.Routing.Control | null>(null);
  const [locationFound, setLocationFound] = useState(null);
  const [locationAllowed, setLocationAllowed] = useState(true);
  const [points, setPoints] = useState([]);
  const [route, setRoute] = useState(null);
  const [bestPath, setBestPath] = useState(false);
  const [showRouteDetails, setShowRouteDetails] = useState(true);
  const [travelMode, setTravelMode] = useState(travelModes[0]); // 'driving', 'walking', etc.
  const max_points = 5;
  const travelModeRef = React.useRef<HTMLSelectElement>(null);

  const handleTravelModeChange = () => {
    if (travelModeRef.current) {
      setTravelMode(travelModeRef.current.value);
    }
  };

  React.useEffect(() => {
    if (locationFound && (!points[0] || (locationFound.lat === points[0].lat && locationFound.lng === points[0].lng))) {
      setPoints([locationFound, ...points.slice(1)]);
    }
  }, [locationFound]);

  const removeLastPoint = () => {
    setPoints(points.slice(0, -1));
  };

  const clearPoints = () => {
    setPoints([]);
  };

  const updatePoint = (index: number, newLatLng: L.LatLng) => {
    const updatedPoints = [...points];
    updatedPoints[index] = newLatLng;
    setPoints(updatedPoints);
  };

  // const map = useMapEvents({
  //   locationfound(e) {
  //     setLocationFound(e.latlng)
  //     map.flyTo(e.latlng, map.getZoom())
  //   }

  // })

  // useEffect(() => {
  //   alert("Hi")
  //   if (!mapContainerRef.current) return;
  //   const initialMap = L.map(mapContainerRef.current).setView(yaounde, 13);

  //   setMap(initialMap);

  //   L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
  //     maxZoom: 19,
  //     attribution: '© OpenStreetMap contributors',
  //   }).addTo(initialMap);

  //   const routingControl = L.Routing.control({
  //     router: L.Routing.osrmv1({
  //       serviceUrl: 'https://router.project-osrm.org/route/v1',
  //     }),
  //     routeWhileDragging: true,
  //     createMarker(i, waypoint, n) {
  //       const marker = L.marker(waypoint.latLng, {
  //         icon: marquerIcon, // Utilisez l'icône personnalisée pour chaque marqueur
  //       });
  //       // Ajouter ici d'autres personnalisations de marqueur si nécessaire
  //       return marker;
  //     },
  //   }).addTo(initialMap);
  //   setControl(routingControl);

  //   initialMap.locate({ setView: true, maxZoom: 20 })

  //   initialMap.on('locationfound', e => {

  //     L.marker(e.latlng, {icon: marquerIcon}).addTo(map).bindPopup('Vous êtes ici!').openPopup();
  //     if (waypoints.length === 0) {
  //       const redMarker = L.circleMarker(e.latlng, {
  //         radius: 8,
  //         fillColor: '#0a2ae0',
  //         color: '#fcf9f9',
  //         weight: 1,
  //         opacity: 1,
  //         fillOpacity: 0.8,
  //       }).addTo(initialMap);

  //       setWaypoints(prevWaypoints => [...prevWaypoints, e.latlng]);
  //       routingControl.spliceWaypoints(0, 0, L.Routing.waypoint(redMarker.getLatLng()));
  //       initialMap.setView(e.latlng, 13);
  //     }
  //   });

  //   initialMap.on('locationerror', () => {
  //     alert("Veuillez autoriser l'acces à votre localisation! Cela permettra de personnaliser votre expérience sur notre site.");
  //     if (waypoints.length === 0) {
  //       initialMap.setView(yaounde, 13);
  //     }
  //   });

  //   initialMap.on('click', e => {
  //     if (waypoints.length < max_number_of_waypoints) {
  //       const newWaypoint = L.Routing.waypoint(e.latlng);
  //       setWaypoints(prevWaypoints => [...prevWaypoints, e.latlng]);
  //       routingControl.spliceWaypoints(routingControl.getWaypoints().length, 0, newWaypoint);
  //     }
  //   });

  //   return () => {
  //     initialMap.remove();
  //   };
  // }, []);

  // const calculateRoute = () => {
  //   if (waypoints.length > 1 && control) {
  //     const waypointsForRouting = waypoints.map(wp => L.Routing.waypoint(wp));
  //     control.setWaypoints(waypointsForRouting);
  //     control.route();
  //   } else {
  //     alert('Veuillez sélectionner au moins deux points sur la carte.');
  //   }
  // };

  // const showSteps = () => {
  //   if (waypoints.length > 1) {
  //     const steps = waypoints
  //       .slice(1, -1)
  //       .map((wp, i) => `Étape ${i + 1}: ${wp.toString()}`)
  //       .join('\n');
  //     const stepsContainer = document.getElementById('stepsContainer');
  //     if (stepsContainer) {
  //       stepsContainer.innerHTML = `<h3>Étapes à suivre :</h3><p>${steps}</p>`;
  //       stepsContainer.style.display = 'block';
  //     }
  //   } else {
  //     alert('Veuillez sélectionner au moins deux points sur la carte.');
  //   }
  // };

  // const goToWaypoints = () => {
  //   if (waypoints.length > 0 && map) {
  //     const bounds = L.latLngBounds(waypoints);
  //     map.fitBounds(bounds.pad(0.1));
  //   } else {
  //     alert('Veuillez sélectionner au moins un point sur la carte.');
  //   }
  // };

  return (
    <div>
      <LocationButton locationAllowed={locationAllowed} setLocationAllowed={setLocationAllowed} />
      <MapContainer center={yaounde} zoom={13} style={{ height: '400px', zIndex: 10 }}>
        <TileLayer
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
          attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
        />
        {/* {showRouteDetails && <RouteDetails route={route} />} */}
        <LocationMarker
          locationAllowed={locationAllowed}
          setLocationAllowed={setLocationAllowed}
          locationFound={locationFound}
          setLocationFound={setLocationFound}
        />
        {/* Ajoutez ici d'autres composants Leaflet personnalisés si nécessaire */}
        {points.map((point, idx) => (
          <Marker
            key={idx}
            position={point}
            icon={marquerIcon}
            draggable={true}
            eventHandlers={{
              dragend(event) {
                const marker = event.target;
                const newPosition = marker.getLatLng();
                updatePoint(idx, newPosition);
              },
            }}
            zIndexOffset={500}
          >
            <Popup autoClose={false}>{`Escale: ${idx}`}</Popup>
          </Marker>
        ))}
        <MapEvents points={points} setPoints={setPoints} max_points={max_points} />
        <RoutingMap
          route={route}
          setRoute={setRoute}
          points={points}
          bestPath={bestPath}
          showRouteDetails={showRouteDetails}
          travelMode={travelMode}
        />
      </MapContainer>
      {/* <button onClick={calculateRoute}>Meilleur chemin</button>
      <button onClick={showSteps}>Étapes</button>
      <button onClick={goToWaypoints}>Go!</button> */}
      {/* ...autres éléments de l'interface utilisateur */}
      <div>
        {/* <div style={{ position: 'absolute', bottom: 10, left: 10, zIndex: 500 }}> */}
        <button
          style={{ position: 'absolute', top: 30, right: 30, zIndex: 1000 }}
          onClick={() => setShowRouteDetails(!showRouteDetails)}
          title="Afficher les détails du trajet"
        >
          <AiOutlineInfoCircle size={25} />
        </button>
        <button onClick={removeLastPoint}>Retirer le dernier point</button>
        <button onClick={clearPoints}>Vider les points</button>
        <button onClick={() => setBestPath(!bestPath)}>Mailleur Itinéraire</button>
        <select ref={travelModeRef} onChange={handleTravelModeChange} value={travelMode}>
          {travelModes.map(mode => (
            <option key={mode} value={mode}>
              {mode}
            </option>
          ))}
        </select>
      </div>
    </div>
  );
};

export default Map;
