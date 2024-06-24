import React, { useState } from 'react';
import axios from 'axios';
import { TextField, Button, List, ListItem, ListItemText, IconButton } from '@material-ui/core';
import DeleteIcon from '@material-ui/icons/Delete';

const SearchMap = ({ selectedPlaces, setSelectedPlaces }) => {
  const [query, setQuery] = useState('');
  const [searchResults, setSearchResults] = useState([]);

  const searchLocation = async newQuery => {
    const response = await axios.get(`https://nominatim.openstreetmap.org/search?format=json&q=${newQuery}`);
    if (response.data && response.data.length > 0) {
      // console.log(response.data);
      setSearchResults(response.data);
    } else {
      setSearchResults([]);
    }
  };

  const addLocation = location => {
    setSelectedPlaces([...selectedPlaces, location]);
    setQuery('');
    setSearchResults([]);
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
        {searchResults.map((result, index) => (
          <ListItem button key={index} onClick={() => addLocation(result)}>
            <ListItemText primary={result.display_name} />
          </ListItem>
        ))}
      </List>

      <List>
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
    </div>
  );
};

export default SearchMap;
