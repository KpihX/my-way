import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { DropdownItem, NavItem, NavLink } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { NavDropdown } from './menu-components';
import { Translate, translate } from 'react-jhipster';
import { Link } from 'react-router-dom';

const metricsItem = () => (
  <MenuItem icon="tachometer-alt" to="/admin/metrics">
    <Translate contentKey="global.menu.admin.metrics">Metrics</Translate>
  </MenuItem>
);

// const openAPIItem = () => (
//   <MenuItem icon="book" to="/admin/docs">
//     <Translate contentKey="global.menu.admin.apidocs">API</Translate>
//   </MenuItem>
// );

export const openAPIItem = () => (
  <NavItem>
    <NavLink tag={Link} to="/admin/docs" className="d-flex align-items-center">
      <FontAwesomeIcon icon="book" />
      <span>
        <Translate contentKey="global.menu.admin.apidocs">API</Translate>
      </span>
    </NavLink>
  </NavItem>
);

export const DevsMenu = ({ showOpenAPI }) =>
  // <NavDropdown icon="users-cog" name={translate('global.menu.admin.main')} id="admin-menu" data-cy="adminMenu">
  // <NavDropdown icon="users-cog" name={"Developpement"} id="admin-menu" data-cy="adminMenu">
  // {metricsItem()}
  showOpenAPI && openAPIItem();
  // </NavDropdown>

export default DevsMenu;
