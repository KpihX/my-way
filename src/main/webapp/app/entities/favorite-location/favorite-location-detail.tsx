import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './favorite-location.reducer';

export const FavoriteLocationDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const favoriteLocationEntity = useAppSelector(state => state.favoriteLocation.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="favoriteLocationDetailsHeading">
          <Translate contentKey="myWayApp.favoriteLocation.detail.title">FavoriteLocation</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{favoriteLocationEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="myWayApp.favoriteLocation.name">Name</Translate>
            </span>
          </dt>
          <dd>{favoriteLocationEntity.name}</dd>
          <dt>
            <Translate contentKey="myWayApp.favoriteLocation.user">User</Translate>
          </dt>
          <dd>{favoriteLocationEntity.user ? favoriteLocationEntity.user.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/favorite-location" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/favorite-location/${favoriteLocationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default FavoriteLocationDetail;
