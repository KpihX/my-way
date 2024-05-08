import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import LocationCategory from './location-category';
import LocationCategoryDetail from './location-category-detail';
import LocationCategoryUpdate from './location-category-update';
import LocationCategoryDeleteDialog from './location-category-delete-dialog';

const LocationCategoryRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<LocationCategory />} />
    <Route path="new" element={<LocationCategoryUpdate />} />
    <Route path=":id">
      <Route index element={<LocationCategoryDetail />} />
      <Route path="edit" element={<LocationCategoryUpdate />} />
      <Route path="delete" element={<LocationCategoryDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default LocationCategoryRoutes;
