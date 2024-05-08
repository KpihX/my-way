import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Location from './location';
import LocationCategory from './location-category';
import Itinary from './itinary';
import FavoriteLocation from './favorite-location';
import FrequentItinary from './frequent-itinary';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="location/*" element={<Location />} />
        <Route path="location-category/*" element={<LocationCategory />} />
        <Route path="itinary/*" element={<Itinary />} />
        <Route path="favorite-location/*" element={<FavoriteLocation />} />
        <Route path="frequent-itinary/*" element={<FrequentItinary />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
