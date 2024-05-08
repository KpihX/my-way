import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './frequent-itinary.reducer';

export const FrequentItinaryDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const frequentItinaryEntity = useAppSelector(state => state.frequentItinary.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="frequentItinaryDetailsHeading">
          <Translate contentKey="myWayApp.frequentItinary.detail.title">FrequentItinary</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{frequentItinaryEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="myWayApp.frequentItinary.name">Name</Translate>
            </span>
          </dt>
          <dd>{frequentItinaryEntity.name}</dd>
          <dt>
            <Translate contentKey="myWayApp.frequentItinary.user">User</Translate>
          </dt>
          <dd>{frequentItinaryEntity.user ? frequentItinaryEntity.user.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/frequent-itinary" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/frequent-itinary/${frequentItinaryEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default FrequentItinaryDetail;
