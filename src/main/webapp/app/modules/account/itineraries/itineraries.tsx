import React from 'react';
import './itineraries.css';

const Itineraries = ({ activeTab }) => {
  return (
    <div id="itinerariesSection" className="Itineraries" style={{ display: activeTab === 'itineraries' ? 'block' : 'none' }}>
      <div className="myNav bg-light">
        <nav className="d-flex justify-content-center">
          <div className="flex-item">
            <form className="form-inline mt-2 mt-md-0 search-form d-flex">
              <input className="form-control mr-sm-2" type="search" placeholder="Rechercher un itinéraire?" aria-label="Search" />
              <button className="btn btn-success my-2 my-sm-0 mybtn" type="submit">
                Search an Itinerary
              </button>
            </form>
          </div>
        </nav>
      </div>
      <div className="container d-flex justify-content-center">
        <h1>Favorite Itineraries</h1>
      </div>
      <div className="container places-content d-flex justify-content-center">
        <table className="table">
          <thead>
            <tr>
              <th scope="col">Item</th>
              <th scope="col">Start Point</th>
              <th scope="col">End Point</th>
              <th scope="col">Point</th>
              <th scope="col">Point</th>
              <th scope="col">Point</th>
              <th scope="col">Description</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <th scope="row">1</th>
              <td>Polytech</td>
              <td>Essos</td>
              <td>Lycée Leclerc</td>
              <td>Poste</td>
              <td>Mvogbi</td>
              <td>
                <i className="fas fa-trash red"></i>
              </td>
            </tr>
            <tr>
              <th scope="row">2</th>
              <td>Odza</td>
              <td>Emana</td>
              <td>Mvan</td>
              <td>Poste</td>
              <td>Lonka</td>
              <td>
                <i className="fas fa-trash red"></i>
              </td>
            </tr>
            <tr>
              <th scope="row">3</th>
              <td>Bastos</td>
              <td>Etoudi</td>
              <td>Mokolo</td>
              <td>Polytech</td>
              <td>Elig Essono</td>
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

export default Itineraries;
