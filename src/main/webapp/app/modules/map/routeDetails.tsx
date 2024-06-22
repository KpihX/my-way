import React from 'react';
import { AiOutlineInfoCircle } from 'react-icons/ai'; // importer l'icône d'information

const RouteDetails = ({ route }) => {
  if (!route) return null;

  // Affichez les détails du trajet ici, par exemple :
  return (
    <div style={{ position: 'absolute', top: 10, right: 10, zIndex: 1000 }}>
      <h3>Détails du trajet</h3>
      {/* Bouclez sur les instructions du trajet */}
    </div>
  );
};

export default RouteDetails;
