import React from 'react';
import './places.css';

const Places = ({ activeTab }) => {
  return (
    <div id="placesSection" className="places" style={{ display: activeTab === 'places' ? 'block' : 'none' }}>
      <div className="myNav bg-light">
        <nav className="d-flex justify-content-center">
          <div className="flex-item">
            <form className="form-inline mt-2 mt-md-0 search-form d-flex">
              <input className="form-control mr-sm-2" type="search" placeholder="Rechercher un Lieu?" aria-label="Search" />
              <button className="btn btn-success my-2 my-sm-0 mybtn" type="submit">
                Search an Place
              </button>
            </form>
          </div>
        </nav>
      </div>
      <div className="container d-flex justify-content-center">
        <h1>Favorite Places</h1>
      </div>
      <div className="container places-content d-flex justify-content-center">
        <table className="table">
          <thead>
            <tr>
              <th scope="col">Item</th>
              <th scope="col">Name</th>
              <th scope="col">Description</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <th scope="row">1</th>
              <td>Polytech</td>
              <td>Mon école</td>
              <td>
                <i className="fas fa-trash red"></i>
              </td>
            </tr>
            <tr>
              <th scope="row">2</th>
              <td>Ecole de Poste</td>
              <td>Chez mon pote</td>
              <td>
                <i className="fas fa-trash red"></i>
              </td>
            </tr>
            <tr>
              <th scope="row">3</th>
              <td>Poste Centrale</td>
              <td>Ma salle de réunion</td>
              <td>
                <i className="fas fa-trash red"></i>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default Places;
