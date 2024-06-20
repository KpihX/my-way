import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { IPlaceCategory } from 'app/shared/model/place-category.model';
import { getEntities as getPlaceCategories } from 'app/entities/place-category/place-category.reducer';
import { IItinary } from 'app/shared/model/itinary.model';
import { getEntities as getItinaries } from 'app/entities/itinary/itinary.reducer';
import { IPlace } from 'app/shared/model/place.model';
import { getEntity, updateEntity, createEntity, reset } from './place.reducer';

export const PlaceUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const placeCategories = useAppSelector(state => state.placeCategory.entities);
  const itinaries = useAppSelector(state => state.itinary.entities);
  const placeEntity = useAppSelector(state => state.place.entity);
  const loading = useAppSelector(state => state.place.loading);
  const updating = useAppSelector(state => state.place.updating);
  const updateSuccess = useAppSelector(state => state.place.updateSuccess);

  const handleClose = () => {
    navigate('/place');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
    dispatch(getPlaceCategories({}));
    dispatch(getItinaries({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.lat !== undefined && typeof values.lat !== 'number') {
      values.lat = Number(values.lat);
    }
    if (values.lon !== undefined && typeof values.lon !== 'number') {
      values.lon = Number(values.lon);
    }

    const entity = {
      ...placeEntity,
      ...values,
      owner: users.find(it => it.id.toString() === values.owner?.toString()),
      category: placeCategories.find(it => it.id.toString() === values.category?.toString()),
      itinary: itinaries.find(it => it.id.toString() === values.itinary?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...placeEntity,
          owner: placeEntity?.owner?.id,
          category: placeEntity?.category?.id,
          itinary: placeEntity?.itinary?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="myWayApp.place.home.createOrEditLabel" data-cy="PlaceCreateUpdateHeading">
            <Translate contentKey="myWayApp.place.home.createOrEditLabel">Create or edit a Place</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="place-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('myWayApp.place.name')}
                id="place-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('myWayApp.place.displayName')}
                id="place-displayName"
                name="displayName"
                data-cy="displayName"
                type="text"
              />
              <ValidatedField
                label={translate('myWayApp.place.lat')}
                id="place-lat"
                name="lat"
                data-cy="lat"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('myWayApp.place.lon')}
                id="place-lon"
                name="lon"
                data-cy="lon"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField label={translate('myWayApp.place.image')} id="place-image" name="image" data-cy="image" type="text" />
              <ValidatedField id="place-owner" name="owner" data-cy="owner" label={translate('myWayApp.place.owner')} type="select">
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="place-category"
                name="category"
                data-cy="category"
                label={translate('myWayApp.place.category')}
                type="select"
              >
                <option value="" key="0" />
                {placeCategories
                  ? placeCategories.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="place-itinary" name="itinary" data-cy="itinary" label={translate('myWayApp.place.itinary')} type="select">
                <option value="" key="0" />
                {itinaries
                  ? itinaries.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/place" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default PlaceUpdate;
