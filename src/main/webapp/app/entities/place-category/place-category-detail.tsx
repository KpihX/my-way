import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './place-category.reducer';

export const PlaceCategoryDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const placeCategoryEntity = useAppSelector(state => state.placeCategory.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="placeCategoryDetailsHeading">
          <Translate contentKey="myWayApp.placeCategory.detail.title">PlaceCategory</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{placeCategoryEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="myWayApp.placeCategory.name">Name</Translate>
            </span>
          </dt>
          <dd>{placeCategoryEntity.name}</dd>
        </dl>
        <Button tag={Link} to="/place-category" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/place-category/${placeCategoryEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PlaceCategoryDetail;
