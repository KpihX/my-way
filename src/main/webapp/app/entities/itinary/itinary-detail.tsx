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
            <span id="prefName">
              <Translate contentKey="myWayApp.itinary.prefName">Pref Name</Translate>
            </span>
          </dt>
          <dd>{itinaryEntity.prefName}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="myWayApp.itinary.description">Description</Translate>
            </span>
          </dt>
          <dd>{itinaryEntity.description}</dd>
          <dt>
            <span id="totalDistance">
              <Translate contentKey="myWayApp.itinary.totalDistance">Total Distance</Translate>
            </span>
          </dt>
          <dd>{itinaryEntity.totalDistance}</dd>
          <dt>
            <span id="totalTime">
              <Translate contentKey="myWayApp.itinary.totalTime">Total Time</Translate>
            </span>
          </dt>
          <dd>{itinaryEntity.totalTime}</dd>
          <dt>
            <span id="image">
              <Translate contentKey="myWayApp.itinary.image">Image</Translate>
            </span>
          </dt>
          <dd>{itinaryEntity.image}</dd>
          <dt>
            <Translate contentKey="myWayApp.itinary.owner">Owner</Translate>
          </dt>
          <dd>{itinaryEntity.owner ? itinaryEntity.owner.id : ''}</dd>
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
