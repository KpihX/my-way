import './footer.scss';

import React from 'react';
import { Translate } from 'react-jhipster';
import { Col, Row } from 'reactstrap';

const Footer = () => (
  <footer className="bg-dark text-white py-4 ">
    <div className="container">
      <div className="row">
        <div className="col-md-6">
          <h5>À propos de nous</h5>
          <p>
            Nous sommes une plateforme dédiée à l'organisation et à l'optimisation de vos voyages. Notre équipe travaille sans relâche pour
            vous offrir des itinéraires sur mesure, des recommandations locales et un suivi en temps réel pour que vous puissiez voyager en
            toute sérénité.
          </p>
        </div>
        <div className="col-md-6">
          <h5>Nous contacter</h5>
          <ul className="list-unstyled">
            <li>
              <i className="fas fa-map-marker-alt"></i> Adresse: enspy, Yaounde
            </li>
            <li>
              <i className="fas fa-phone"></i> Téléphone: +237 620 86 47 61
            </li>
            <li>
              <i className="fas fa-envelope"></i> Email: contact@myway.com
            </li>
          </ul>
        </div>
      </div>
    </div>
  </footer>
);

export default Footer;
