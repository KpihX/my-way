import React, { useState, FC } from 'react';
import { MapContainer, TileLayer, Marker, Popup, Tooltip, useMapEvents } from 'react-leaflet';
import LocationButton from './locationButton';
import { marquerIcon, middleMarquer, endMarquer } from './icons';
import LocationMarker from './locationMarker';
import RoutingMap from './routingMap';
import { AiOutlineInfoCircle } from 'react-icons/ai';
import SearchMap from './SearchMap';
import './map.scss';

// function MapEvents({ points, selectedPlaces, setPoints, max_points, setSelectedPlaces }) {
function MapEvents({ selectedPlaces, max_points, setSelectedPlaces }) {
  useMapEvents({
    click(e) {
      addPoint(e.latlng);
    },
  });

  const addPoint = point => {
    // if (points.length >= max_points) {
    if (selectedPlaces.length >= max_points) {
      alert('Vous avez atteint le nombre maximum de points.');
      return;
    }

    // const newPoints = [...points, point];
    // setPoints(newPoints);

    // Convertir le point au format souhaité
    const newPlace = {
      lat: point.lat,
      lon: point.lng,
      display_name: `Lieu: ${point.lat}, ${point.lng}`,
      name: `${point.lat}, ${point.lng}`,
      // address: {
      //   type: 'point',
      //   importance: 0.5,
      //   place_rank: 30,
      // },
    };

    setSelectedPlaces([...selectedPlaces, newPlace]);
  };

  // console.log("piints: ", points);
  // console.log("spiints: ", selectedPlaces);

  return null;
}

const yaounde: L.LatLngExpression = [3.848, 11.502];

const Map = () => {
  const [locationFound, setLocationFound] = useState(null);
  const [locationAllowed, setLocationAllowed] = useState(true);
  // const [points, setPoints] = useState([]);
  const [route, setRoute] = useState(null);
  const [bestPath, setBestPath] = useState(false);
  const [showRouteDetails, setShowRouteDetails] = useState(true);
  const [travelMode, setTravelMode] = useState('driving');
  const [selectedPlaces, setSelectedPlaces] = useState([]);

  const max_points = 5;
  const travelModeRef = React.useRef(null);

  const handleTravelModeChange = () => {
    if (travelModeRef.current) {
      setTravelMode(travelModeRef.current.value);
    }
  };

  React.useEffect(() => {
    // if (locationFound && (!points[0] || (locationFound.lat === points[0].lat && locationFound.lng === points[0].lng))) {
    //   setPoints([locationFound, ...points.slice(1)]);
    // setPoints([locationFound, ...points.slice(1)]);
    if (
      locationFound &&
      (!selectedPlaces[0] || (locationFound.lat === selectedPlaces[0].lat && locationFound.lng === selectedPlaces[0].lon))
    ) {
      const newPlace = {
        lat: locationFound.lat,
        lon: locationFound.lng,
        display_name: `Your position: ${locationFound.lat}, ${locationFound.lng}`,
        name: `${locationFound.lat}, ${locationFound.lng}`,
      };

      setSelectedPlaces([...selectedPlaces, newPlace]);
    }
  }, [locationFound]);

  // React.useEffect(() => {
  //   if (selectedPlaces.length !== points.length) {
  //     setPoints(selectedPlaces.map(place => ({ lat: place.lat, lon: place.lon })));
  //   }
  // }, [selectedPlaces]);

  const updatePoint = (index, newLatLng) => {
    // const updatedPoints = [...points];
    // updatedPoints[index] = newLatLng;
    // setPoints(updatedPoints);
    const updatedPlaces = [...selectedPlaces];
    updatedPlaces[index] = { ...updatedPlaces[index], ...newLatLng };
    setSelectedPlaces(updatedPlaces);
  };

  return (
    <div>
      <SearchMap selectedPlaces={selectedPlaces} setSelectedPlaces={setSelectedPlaces} />
      <LocationButton locationAllowed={locationAllowed} setLocationAllowed={setLocationAllowed} />
      <MapContainer center={yaounde} zoom={13} style={{ height: '400px', zIndex: 10 }}>
        <TileLayer
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
          attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
        />
        <LocationMarker
          locationAllowed={locationAllowed}
          setLocationAllowed={setLocationAllowed}
          locationFound={locationFound}
          setLocationFound={setLocationFound}
        />
        {selectedPlaces.map((point, idx) => (
          <Marker
            key={idx}
            position={{ lng: point.lon, lat: point.lat }}
            icon={idx === 0 ? marquerIcon : idx === selectedPlaces.length - 1 ? endMarquer : middleMarquer}
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
            <Popup autoClose={false} closeOnClick={false} closeButton={false}>
              {idx === 0 ? 'Départ' : idx === selectedPlaces.length - 1 ? 'Destination' : `Escale ${idx}`}
            </Popup>
            <Tooltip>{idx === 0 ? 'Départ' : idx === selectedPlaces.length - 1 ? 'Destination' : `Escale ${idx}`}</Tooltip>
          </Marker>
        ))}
        <MapEvents
          // points={points}
          selectedPlaces={selectedPlaces}
          // setPoints={setPoints}
          max_points={max_points}
          setSelectedPlaces={setSelectedPlaces}
        />
        <RoutingMap
          route={route}
          setRoute={setRoute}
          // points={points}
          selectedPlaces={selectedPlaces}
          bestPath={bestPath}
          showRouteDetails={showRouteDetails}
          travelMode={travelMode}
        />
      </MapContainer>
      <button onClick={() => setShowRouteDetails(!showRouteDetails)}>
        <AiOutlineInfoCircle size={25} />
      </button>
      <button onClick={() => setSelectedPlaces(selectedPlaces.slice(0, -1))}>Retirer le dernier marqueur</button>
      <button onClick={() => setSelectedPlaces([])}>Rétirer tous les marqueurs</button>
      <button onClick={() => setBestPath(!bestPath)}>Meilleur Itinéraire ?</button>
      <select ref={travelModeRef} onChange={handleTravelModeChange} value={travelMode}>
        {['driving', 'walking', 'cycling'].map(mode => (
          <option key={mode} value={mode}>
            {mode}
          </option>
        ))}
      </select>
    </div>
  );
};

export default Map;
