import React, { useState } from 'react';
import axios from 'axios';
import { TextField, Button, List, ListItem, ListItemText, IconButton, Collapse } from '@material-ui/core';
import { ExpandLess, ExpandMore, Delete as DeleteIcon } from '@material-ui/icons';

const SearchMap = ({ selectedPlaces, setSelectedPlaces, bestPath, setBestPath, travelMode, setTravelMode }) => {
  const [query, setQuery] = useState('');
  const [searchResults, setSearchResults] = useState([]);
  const [searchResultsOpen, setSearchResultsOpen] = useState(false);
  const [selectedPlacesOpen, setSelectedPlacesOpen] = useState(false);

  const travelModeRef = React.useRef(null);

  const handleTravelModeChange = () => {
    if (travelModeRef.current) {
      setTravelMode(travelModeRef.current.value);
    }
  };

  const searchLocation = async newQuery => {
    const response = await axios.get(`https://nominatim.openstreetmap.org/search?format=json&q=${newQuery}`);
    if (response.data && response.data.length > 0) {
      setSearchResults(response.data);
      setSearchResultsOpen(true);
    } else {
      setSearchResults([]);
      setSearchResultsOpen(false);
    }
  };

  const addLocation = location => {
    setSelectedPlaces([...selectedPlaces, location]);
    setQuery('');
    setSearchResults([]);
    setSearchResultsOpen(false);
    setSelectedPlacesOpen(true);
  };

  const removeLocation = index => {
    const updatedPlaces = [...selectedPlaces];
    updatedPlaces.splice(index, 1);
    setSelectedPlaces(updatedPlaces);
  };

  const handleCoordinateInput = () => {
    const [lat, lon] = query.split(',').map(coord => parseFloat(coord.trim()));
    if (!isNaN(lat) && !isNaN(lon)) {
      addLocation({ lat, lon, display_name: `Coord: ${lat}, ${lon}` });
    } else {
      alert('Invalid coordinates');
    }
  };

  return (
    <div>
      <TextField type="text" value={query} onChange={e => setQuery(e.target.value)} placeholder="Enter location or coordinates" fullWidth />
      <Button onClick={() => searchLocation(query)}>Search</Button>
      <Button onClick={handleCoordinateInput}>Add Coordinates</Button>

      <List>
        <ListItem button onClick={() => setSearchResultsOpen(!searchResultsOpen)}>
          <ListItemText primary="Search Results" />
          {searchResultsOpen ? <ExpandLess /> : <ExpandMore />}
        </ListItem>
        <Collapse in={searchResultsOpen} timeout="auto" unmountOnExit>
          <List component="div" disablePadding>
            {searchResults.map((result, index) => (
              <ListItem button key={index} onClick={() => addLocation(result)}>
                <ListItemText primary={result.display_name} />
              </ListItem>
            ))}
          </List>
        </Collapse>
      </List>

      <List>
        <ListItem button onClick={() => setSelectedPlacesOpen(!selectedPlacesOpen)}>
          <ListItemText primary="Selected Places" />
          {selectedPlacesOpen ? <ExpandLess /> : <ExpandMore />}
        </ListItem>
        <Collapse in={selectedPlacesOpen} timeout="auto" unmountOnExit>
          <List component="div" disablePadding>
            {selectedPlaces.map((place, index) => (
              <ListItem key={index}>
                <ListItemText
                  primary={`${index === 0 ? 'Start' : index === selectedPlaces.length - 1 ? 'End' : `Stop ${index}`}: ${place.display_name || `${place.lat}, ${place.lon}`}`}
                />
                <IconButton edge="end" onClick={() => removeLocation(index)}>
                  <DeleteIcon />
                </IconButton>
              </ListItem>
            ))}
          </List>
        </Collapse>
      </List>
      <select ref={travelModeRef} onChange={handleTravelModeChange} value={travelMode}>
        {['driving', 'walking', 'cycling'].map(mode => (
          <option key={mode} value={mode}>
            {mode}
          </option>
        ))}
      </select>
      <button onClick={() => setBestPath(!bestPath)}>Meilleur Itinéraire ?</button>
    </div>
  );
};

export default SearchMap;
