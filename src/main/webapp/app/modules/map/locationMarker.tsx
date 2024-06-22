import React from 'react';
import { marquerIcon, positionMarquer } from './icons';
import { Marker, Tooltip, Circle, useMap, useMapEvents } from 'react-leaflet';
import L from 'leaflet';

export default function LocationMarker({ locationAllowed, setLocationAllowed, locationFound, setLocationFound }) {
  const map = useMap();
  const [accuracy, setAccuracy] = React.useState(0);

  useMapEvents({
    locationfound(e) {
      setLocationFound(e.latlng);
      setAccuracy(e.accuracy);
    },
    locationerror(e) {
      setLocationAllowed(false);
      alert("Veuillez autoriser l'accès à votre localisation! Cela permettra de personnaliser votre expérience sur notre site.");
    },
    moveend() {
      if (locationFound) {
        const isInBounds = map.getBounds().contains(locationFound);
        setLocationAllowed(isInBounds);
      }
    },
  });

  React.useEffect(() => {
    if (locationAllowed) {
      map.locate();
    }
  }, [locationAllowed, map]);

  React.useEffect(() => {
    if (locationAllowed && locationFound) {
      map.flyTo(locationFound, map.getZoom());
    }
  }, [map, locationFound]);

  return (
    <>
      {locationFound && (
        <Marker position={locationFound} icon={positionMarquer}>
          <Tooltip>{locationAllowed ? 'Votre position' : 'Votre dernière position'}</Tooltip>
        </Marker>
      )}
      {locationFound && accuracy && (
        <Circle
          center={locationFound}
          radius={accuracy} // Utilisez le rayon de certitude fourni par OSM
          fillColor="lightcoral"
          color="red"
          weight={1}
          opacity={0.5}
          fillOpacity={0.2}
        />
      )}
    </>
  );
}
