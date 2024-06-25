import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Profile from './profile/profile';
import Password from './password/password';

const AccountRoutes = () => (
  <div>
    <ErrorBoundaryRoutes>
      <Route path="profile" element={<Profile />} />
      <Route path="password" element={<Password />} />
    </ErrorBoundaryRoutes>
  </div>
);

export default AccountRoutes;
