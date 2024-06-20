import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Place from './place';
import PlaceCategory from './place-category';
import Itinary from './itinary';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="place/*" element={<Place />} />
        <Route path="place-category/*" element={<PlaceCategory />} />
        <Route path="itinary/*" element={<Itinary />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
