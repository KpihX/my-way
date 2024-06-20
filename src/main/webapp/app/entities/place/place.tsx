import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './place.reducer';

export const Place = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const placeList = useAppSelector(state => state.place.entities);
  const loading = useAppSelector(state => state.place.loading);

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
      <h2 id="place-heading" data-cy="PlaceHeading">
        <Translate contentKey="myWayApp.place.home.title">Places</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="myWayApp.place.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/place/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="myWayApp.place.home.createLabel">Create new Place</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {placeList && placeList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="myWayApp.place.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('name')}>
                  <Translate contentKey="myWayApp.place.name">Name</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('name')} />
                </th>
                <th className="hand" onClick={sort('displayName')}>
                  <Translate contentKey="myWayApp.place.displayName">Display Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('displayName')} />
                </th>
                <th className="hand" onClick={sort('lat')}>
                  <Translate contentKey="myWayApp.place.lat">Lat</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('lat')} />
                </th>
                <th className="hand" onClick={sort('lon')}>
                  <Translate contentKey="myWayApp.place.lon">Lon</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('lon')} />
                </th>
                <th className="hand" onClick={sort('image')}>
                  <Translate contentKey="myWayApp.place.image">Image</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('image')} />
                </th>
                <th>
                  <Translate contentKey="myWayApp.place.owner">Owner</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="myWayApp.place.category">Category</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="myWayApp.place.itinary">Itinary</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {placeList.map((place, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/place/${place.id}`} color="link" size="sm">
                      {place.id}
                    </Button>
                  </td>
                  <td>{place.name}</td>
                  <td>{place.displayName}</td>
                  <td>{place.lat}</td>
                  <td>{place.lon}</td>
                  <td>{place.image}</td>
                  <td>{place.owner ? place.owner.id : ''}</td>
                  <td>{place.category ? <Link to={`/place-category/${place.category.id}`}>{place.category.id}</Link> : ''}</td>
                  <td>{place.itinary ? <Link to={`/itinary/${place.itinary.id}`}>{place.itinary.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/place/${place.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/place/${place.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/place/${place.id}/delete`)}
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
              <Translate contentKey="myWayApp.place.home.notFound">No Places found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Place;
