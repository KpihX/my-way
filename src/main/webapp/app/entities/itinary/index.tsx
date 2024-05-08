import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Itinary from './itinary';
import ItinaryDetail from './itinary-detail';
import ItinaryUpdate from './itinary-update';
import ItinaryDeleteDialog from './itinary-delete-dialog';

const ItinaryRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Itinary />} />
    <Route path="new" element={<ItinaryUpdate />} />
    <Route path=":id">
      <Route index element={<ItinaryDetail />} />
      <Route path="edit" element={<ItinaryUpdate />} />
      <Route path="delete" element={<ItinaryDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ItinaryRoutes;
