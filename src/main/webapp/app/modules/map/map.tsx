import React, { useState, useEffect, useRef, FC } from 'react';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import 'leaflet-routing-machine';
import 'leaflet-routing-machine/dist/leaflet-routing-machine.css';
// Importer les styles du plugin Leaflet Search
import 'leaflet-search/dist/leaflet-search.min.css';
import 'leaflet-search';

const Map: FC = () => {
  const mapContainerRef = useRef<HTMLDivElement>(null);
  const [map, setMap] = useState<L.Map | null>(null);
  const [waypoints, setWaypoints] = useState<L.LatLng[]>([]);
  const [control, setControl] = useState<L.Routing.Control | null>(null);
  const yaounde: L.LatLngExpression = [3.848, 11.502];
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
