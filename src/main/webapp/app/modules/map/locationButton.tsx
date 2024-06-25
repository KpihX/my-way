import React, { useState } from 'react';
import { MdMyLocation } from 'react-icons/md';

const LocationButton = ({ locationAllowed, setLocationAllowed }) => {
  const handleClick = event => {
    event.stopPropagation(); // Ceci empêchera l'événement de se propager
    setLocationAllowed(!locationAllowed);
  };

  return (
    <div style={{ position: 'absolute', top: '80px', left: '7px', zIndex: 9999 }}>
      <button
        onClick={handleClick}
        title="Utiliser ma position actuelle"
        style={{
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          width: '40px',
          height: '40px',
          backgroundColor: locationAllowed ? '#4CAF50' : '#f44336',
          color: 'white',
          border: 'none',
          borderRadius: '20px',
          cursor: 'pointer',
        }}
      >
        <MdMyLocation style={{ fontSize: '1.5em' }} />
      </button>
    </div>
  );
};

export default LocationButton;
