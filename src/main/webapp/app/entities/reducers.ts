import location from 'app/entities/location/location.reducer';
import locationCategory from 'app/entities/location-category/location-category.reducer';
import itinary from 'app/entities/itinary/itinary.reducer';
import favoriteLocation from 'app/entities/favorite-location/favorite-location.reducer';
import frequentItinary from 'app/entities/frequent-itinary/frequent-itinary.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  location,
  locationCategory,
  itinary,
  favoriteLocation,
  frequentItinary,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
