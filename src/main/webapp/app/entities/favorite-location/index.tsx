import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import FavoriteLocation from './favorite-location';
import FavoriteLocationDetail from './favorite-location-detail';
import FavoriteLocationUpdate from './favorite-location-update';
import FavoriteLocationDeleteDialog from './favorite-location-delete-dialog';

const FavoriteLocationRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<FavoriteLocation />} />
    <Route path="new" element={<FavoriteLocationUpdate />} />
    <Route path=":id">
      <Route index element={<FavoriteLocationDetail />} />
      <Route path="edit" element={<FavoriteLocationUpdate />} />
      <Route path="delete" element={<FavoriteLocationDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default FavoriteLocationRoutes;
