import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/location">
        <Translate contentKey="global.menu.entities.location" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/location-category">
        <Translate contentKey="global.menu.entities.locationCategory" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/itinary">
        <Translate contentKey="global.menu.entities.itinary" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/favorite-location">
        <Translate contentKey="global.menu.entities.favoriteLocation" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/frequent-itinary">
        <Translate contentKey="global.menu.entities.frequentItinary" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
