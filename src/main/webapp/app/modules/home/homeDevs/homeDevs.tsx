// HomeDevs.js
import React from 'react';
import './homeDevs.css';

const HomeDevs = () => {
  return (
    <div className="body">
      <section className="container mt-5">
        <h1 className="text-center mb-4">Que voulez-vous faire ?</h1>
        <div className="row justify-content-center">
          <div className="col-md-3 text-center">
            <a href="/map" className="btn btn-primary btn-lg mb-3 mybutton">
              Tester le service Map
            </a>
          </div>
          <div className="col-md-3 text-center">
            <a href="/admin/docs" className="btn btn-primary btn-lg mb-3 mybutton">
              Se familiariser avec l'API
            </a>
          </div>
          <div className="col-md-3 text-center">
            <a href="https://github.com/KpihX/my-way" className="btn btn-primary btn-lg mb-3 mybutton">
              Travaillez avec nous
            </a>
          </div>
        </div>
      </section>
    </div>
  );
};

export default HomeDevs;
