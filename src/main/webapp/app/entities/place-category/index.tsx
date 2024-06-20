import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import PlaceCategory from './place-category';
import PlaceCategoryDetail from './place-category-detail';
import PlaceCategoryUpdate from './place-category-update';
import PlaceCategoryDeleteDialog from './place-category-delete-dialog';

const PlaceCategoryRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<PlaceCategory />} />
    <Route path="new" element={<PlaceCategoryUpdate />} />
    <Route path=":id">
      <Route index element={<PlaceCategoryDetail />} />
      <Route path="edit" element={<PlaceCategoryUpdate />} />
      <Route path="delete" element={<PlaceCategoryDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PlaceCategoryRoutes;
