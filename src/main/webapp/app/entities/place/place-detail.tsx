import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './place.reducer';

export const PlaceDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const placeEntity = useAppSelector(state => state.place.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="placeDetailsHeading">
          <Translate contentKey="myWayApp.place.detail.title">Place</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{placeEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="myWayApp.place.name">Name</Translate>
            </span>
          </dt>
          <dd>{placeEntity.name}</dd>
          <dt>
            <span id="displayName">
              <Translate contentKey="myWayApp.place.displayName">Display Name</Translate>
            </span>
          </dt>
          <dd>{placeEntity.displayName}</dd>
          <dt>
            <span id="lat">
              <Translate contentKey="myWayApp.place.lat">Lat</Translate>
            </span>
          </dt>
          <dd>{placeEntity.lat}</dd>
          <dt>
            <span id="lon">
              <Translate contentKey="myWayApp.place.lon">Lon</Translate>
            </span>
          </dt>
          <dd>{placeEntity.lon}</dd>
          <dt>
            <span id="image">
              <Translate contentKey="myWayApp.place.image">Image</Translate>
            </span>
          </dt>
          <dd>{placeEntity.image}</dd>
          <dt>
            <Translate contentKey="myWayApp.place.owner">Owner</Translate>
          </dt>
          <dd>{placeEntity.owner ? placeEntity.owner.id : ''}</dd>
          <dt>
            <Translate contentKey="myWayApp.place.category">Category</Translate>
          </dt>
          <dd>{placeEntity.category ? placeEntity.category.id : ''}</dd>
          <dt>
            <Translate contentKey="myWayApp.place.itinary">Itinary</Translate>
          </dt>
          <dd>{placeEntity.itinary ? placeEntity.itinary.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/place" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/place/${placeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PlaceDetail;
