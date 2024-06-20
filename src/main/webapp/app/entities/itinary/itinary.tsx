import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './itinary.reducer';

export const Itinary = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const itinaryList = useAppSelector(state => state.itinary.entities);
  const loading = useAppSelector(state => state.itinary.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="itinary-heading" data-cy="ItinaryHeading">
        <Translate contentKey="myWayApp.itinary.home.title">Itinaries</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="myWayApp.itinary.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/itinary/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="myWayApp.itinary.home.createLabel">Create new Itinary</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {itinaryList && itinaryList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="myWayApp.itinary.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('name')}>
                  <Translate contentKey="myWayApp.itinary.name">Name</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('name')} />
                </th>
                <th className="hand" onClick={sort('prefName')}>
                  <Translate contentKey="myWayApp.itinary.prefName">Pref Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('prefName')} />
                </th>
                <th className="hand" onClick={sort('description')}>
                  <Translate contentKey="myWayApp.itinary.description">Description</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('description')} />
                </th>
                <th className="hand" onClick={sort('totalDistance')}>
                  <Translate contentKey="myWayApp.itinary.totalDistance">Total Distance</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('totalDistance')} />
                </th>
                <th className="hand" onClick={sort('totalTime')}>
                  <Translate contentKey="myWayApp.itinary.totalTime">Total Time</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('totalTime')} />
                </th>
                <th className="hand" onClick={sort('image')}>
                  <Translate contentKey="myWayApp.itinary.image">Image</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('image')} />
                </th>
                <th>
                  <Translate contentKey="myWayApp.itinary.owner">Owner</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {itinaryList.map((itinary, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/itinary/${itinary.id}`} color="link" size="sm">
                      {itinary.id}
                    </Button>
                  </td>
                  <td>{itinary.name}</td>
                  <td>{itinary.prefName}</td>
                  <td>{itinary.description}</td>
                  <td>{itinary.totalDistance}</td>
                  <td>{itinary.totalTime}</td>
                  <td>{itinary.image}</td>
                  <td>{itinary.owner ? itinary.owner.id : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/itinary/${itinary.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/itinary/${itinary.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/itinary/${itinary.id}/delete`)}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="myWayApp.itinary.home.notFound">No Itinaries found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Itinary;
