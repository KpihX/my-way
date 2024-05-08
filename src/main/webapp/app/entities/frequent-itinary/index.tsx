import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import FrequentItinary from './frequent-itinary';
import FrequentItinaryDetail from './frequent-itinary-detail';
import FrequentItinaryUpdate from './frequent-itinary-update';
import FrequentItinaryDeleteDialog from './frequent-itinary-delete-dialog';

const FrequentItinaryRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<FrequentItinary />} />
    <Route path="new" element={<FrequentItinaryUpdate />} />
    <Route path=":id">
      <Route index element={<FrequentItinaryDetail />} />
      <Route path="edit" element={<FrequentItinaryUpdate />} />
      <Route path="delete" element={<FrequentItinaryDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default FrequentItinaryRoutes;
