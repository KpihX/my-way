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
import { IItinary } from 'app/shared/model/itinary.model';
import { getEntity, updateEntity, createEntity, reset } from './itinary.reducer';

export const ItinaryUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const itinaryEntity = useAppSelector(state => state.itinary.entity);
  const loading = useAppSelector(state => state.itinary.loading);
  const updating = useAppSelector(state => state.itinary.updating);
  const updateSuccess = useAppSelector(state => state.itinary.updateSuccess);

  const handleClose = () => {
    navigate('/itinary');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
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
    if (values.totalDistance !== undefined && typeof values.totalDistance !== 'number') {
      values.totalDistance = Number(values.totalDistance);
    }
    if (values.totalTime !== undefined && typeof values.totalTime !== 'number') {
      values.totalTime = Number(values.totalTime);
    }

    const entity = {
      ...itinaryEntity,
      ...values,
      owner: users.find(it => it.id.toString() === values.owner?.toString()),
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
          ...itinaryEntity,
          owner: itinaryEntity?.owner?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="myWayApp.itinary.home.createOrEditLabel" data-cy="ItinaryCreateUpdateHeading">
            <Translate contentKey="myWayApp.itinary.home.createOrEditLabel">Create or edit a Itinary</Translate>
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
                  id="itinary-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('myWayApp.itinary.name')}
                id="itinary-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('myWayApp.itinary.prefName')}
                id="itinary-prefName"
                name="prefName"
                data-cy="prefName"
                type="text"
              />
              <ValidatedField
                label={translate('myWayApp.itinary.description')}
                id="itinary-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                label={translate('myWayApp.itinary.totalDistance')}
                id="itinary-totalDistance"
                name="totalDistance"
                data-cy="totalDistance"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('myWayApp.itinary.totalTime')}
                id="itinary-totalTime"
                name="totalTime"
                data-cy="totalTime"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField label={translate('myWayApp.itinary.image')} id="itinary-image" name="image" data-cy="image" type="text" />
              <ValidatedField id="itinary-owner" name="owner" data-cy="owner" label={translate('myWayApp.itinary.owner')} type="select">
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/itinary" replace color="info">
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

export default ItinaryUpdate;
