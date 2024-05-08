import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './itinary.reducer';

export const ItinaryDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const itinaryEntity = useAppSelector(state => state.itinary.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="itinaryDetailsHeading">
          <Translate contentKey="myWayApp.itinary.detail.title">Itinary</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{itinaryEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="myWayApp.itinary.name">Name</Translate>
            </span>
          </dt>
          <dd>{itinaryEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="myWayApp.itinary.description">Description</Translate>
            </span>
          </dt>
          <dd>{itinaryEntity.description}</dd>
          <dt>
            <span id="distance">
              <Translate contentKey="myWayApp.itinary.distance">Distance</Translate>
            </span>
          </dt>
          <dd>{itinaryEntity.distance}</dd>
          <dt>
            <span id="duration">
              <Translate contentKey="myWayApp.itinary.duration">Duration</Translate>
            </span>
          </dt>
          <dd>{itinaryEntity.duration}</dd>
          <dt>
            <span id="polyline">
              <Translate contentKey="myWayApp.itinary.polyline">Polyline</Translate>
            </span>
          </dt>
          <dd>{itinaryEntity.polyline}</dd>
          <dt>
            <Translate contentKey="myWayApp.itinary.location">Location</Translate>
          </dt>
          <dd>
            {itinaryEntity.locations
              ? itinaryEntity.locations.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.name}</a>
                    {itinaryEntity.locations && i === itinaryEntity.locations.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/itinary" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/itinary/${itinaryEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ItinaryDetail;
