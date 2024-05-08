import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './location-category.reducer';

export const LocationCategoryDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const locationCategoryEntity = useAppSelector(state => state.locationCategory.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="locationCategoryDetailsHeading">
          <Translate contentKey="myWayApp.locationCategory.detail.title">LocationCategory</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{locationCategoryEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="myWayApp.locationCategory.name">Name</Translate>
            </span>
          </dt>
          <dd>{locationCategoryEntity.name}</dd>
        </dl>
        <Button tag={Link} to="/location-category" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/location-category/${locationCategoryEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default LocationCategoryDetail;
