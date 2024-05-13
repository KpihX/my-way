import React, { useState } from 'react';
import Itineraries from '../itineraries/itineraries';
import Places from '../places/places';
import SettingsPage from '../settings/settings';

const HeaderProfile = () => {
  const [activeTab, setActiveTab] = useState('settings');

  const handleTabClick = tab => {
    setActiveTab(tab);
  };

  return (
    <div className="header-profile">
      {/* Boutons pour les onglets */}
      <div className="d-flex justify-content-center myNav bg-light">
        <div className={`flex-item ${activeTab === 'settings' ? 'active' : ''}`}>
          <button className="btn btn-default btn-lg myFlex" onClick={() => handleTabClick('settings')}>
            Settings
          </button>
        </div>
        <div className={`flex-item ${activeTab === 'itineraries' ? 'active' : ''}`}>
          <button className="btn btn-default btn-lg myFlex" onClick={() => handleTabClick('itineraries')}>
            Itineraries
          </button>
        </div>
        <div className={`flex-item ${activeTab === 'places' ? 'active' : ''}`}>
          <button className="btn btn-default btn-lg myFlex" onClick={() => handleTabClick('places')}>
            Places
          </button>
        </div>
      </div>

      {/* Affichage des composants correspondant à l'onglet sélectionné */}
      {activeTab === 'settings' && <SettingsPage />}
      {activeTab === 'itineraries' && <Itineraries activeTab={activeTab} />}
      {activeTab === 'places' && <Places activeTab={activeTab} />}
    </div>
  );
};

export default HeaderProfile;
