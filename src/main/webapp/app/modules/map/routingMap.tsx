import React, { useState, useEffect } from 'react';
import { MapContainer, TileLayer, Marker, Polyline, useMap } from 'react-leaflet';
import L from 'leaflet';
import 'leaflet-routing-machine';
// import 'leaflet-routing-machine/dist/leaflet-routing-machine.css';

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

declare global {
  interface Window {
    L: any;
  }
}

const RoutingMap = ({ points, route, setRoute, bestPath, showRouteDetails, travelMode }) => {
  const map = useMap();

  useEffect(() => {
    // Vérifiez s'il y a suffisamment de points pour tracer un itinéraire
    // console.log("dfd: ", L.Routing)
    if (!map || !L.Routing) return;

    if (points.length > 1) {
      // Créez une instance de L.Routing.Control et ne l'ajoutez pas encore à la carte
      const routingControl = L.Routing.control({
        router: L.Routing.osrmv1({
          serviceUrl: 'https://router.project-osrm.org/route/v1',
          profile: travelMode,
        }),
        waypoints: points.map(point => L.Routing.waypoint(point)),
        routeWhileDragging: true,
        createMarker() {
          return null;
        }, // Pas de marqueur pour les waypoints
        // addWaypoints: false,
        show: false,
        showAlternatives: true,
      });

      if (routingControl) {
        if (showRouteDetails) {
          // If showRouteDetails is true, add the routingControl to the map
          routingControl.addTo(map);
        } else {
          // If showRouteDetails becomes false, remove the routingControl from the map
          routingControl.remove();
        }
      }

      // Calculer l'itinéraire
      routingControl.on('routesfound', e => {
        const routes = e.routes;
        if (routes && routes.length) {
          // Stockez l'itinéraire trouvé
          setRoute(routes[0]);
        }
      });

      // Utilisez le service de routage pour calculer l'itinéraire

      routingControl.route();

      // routingControl.on('routingerror', () => {
      //   alert('Erreur de calcul de l’itinéraire!');
      // });

      // Nettoyage
      return () => {
        if (routingControl) {
          routingControl.remove();
        }
      };
    }
  }, [map, setRoute, bestPath, showRouteDetails, travelMode]);

  useEffect(() => {
    console.log(route);
  }, [route]);

  // console.log("***", route)

  return <>{route && points.length > 1 && <Polyline positions={route.coordinates} color="blue" />}</>;
};

export default RoutingMap;
