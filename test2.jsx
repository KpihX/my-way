import React from 'react';
import { Container, Navbar, Nav, Button, Carousel, Row, Col } from 'react-bootstrap';

function App() {
  return (
    <div>
      <Navbar bg="primary" expand="md" fixed="top">
        <Container>
          <Navbar.Brand href="index.html">
            <img src="asset/images/logo.jpeg" alt="logo" className="rounded-circle" />
          </Navbar.Brand>
          <Navbar.Toggle aria-controls="navbarCollapse" />
          <Navbar.Collapse id="navbarCollapse">
            <Nav className="ml-auto">
              <Nav.Link href="index.html">Home</Nav.Link>
              <Nav.Link href="Map.html">Map</Nav.Link>
              <div className="profile-container">
                <Nav.Link href="profile.html">
                  <img src="asset/images/ppgaby.jpg" alt="you" className="rounded-circle d-block" width="30" height="30" />
                  <small className="d-block">Gabriel</small>
                </Nav.Link>
              </div>
            </Nav>
          </Navbar.Collapse>
        </Container>
      </Navbar>

      <Carousel>
        <Carousel.Item>
          <img className="d-block w-100" src="/asset/images/nouvelleimage4.jpeg" alt="First slide" />
          <Carousel.Caption>
            <h1>Voyagez en toute sérénité avec notre suivi d'itinéraire en temps réel.</h1>
            <h4>Planifiez et suivez chaque étape de votre voyage en toute confiance.</h4>
            <Button variant="primary" href="register.html">Inscrivez-vous aujourd'hui</Button>
          </Carousel.Caption>
        </Carousel.Item>
        <Carousel.Item>
          <img className="d-block w-100" src="/asset/images/nouvelleimage5.jpeg" alt="Second slide" />
          <Carousel.Caption>
            <h1>Explorez des itinéraires uniques pour des voyages mémorables.</h1>
            <h4>Découvrez des destinations cachées et des parcours sur mesure.</h4>
            <Button variant="primary" href="Map.html">Visitez la carte</Button>
          </Carousel.Caption>
        </Carousel.Item>
        <Carousel.Item>
          <img className="d-block w-100" src="asset/images/nouvelleimage6.jpeg" alt="Third slide" />
          <Carousel.Caption>
            <h1>Optimisez votre voyage avec des itinéraires personnalisés et faciles à suivre.</h1>
            <h4>Simplifiez vos déplacements avec de simples vues  sur le trajet.</h4>
            <Button variant="primary" href="Map.html">Explorer</Button>
          </Carousel.Caption>
        </Carousel.Item>
      </Carousel>

      <Container className="mt-5">
        <h1 className="text-center mb-4">Que voulez-vous faire ?</h1>
        <Row className="justify-content-center">
          <Col md={3} className="text-center">
            <Button variant="primary" size="lg" className="mb-3 mybutton" href="Map.html">Explorer notre site</Button>
          </Col>
          <Col md={3} className="text-center">
            <Button variant="primary" size="lg" className="mb-3 mybutton" href="Map.html">Rechercher un lieu</Button>
          </Col>
          <Col md={3} className="text-center">
            <Button variant="primary" size="lg" className="mb-3 mybutton" href="Map.html">Rechercher un itinéraire</Button>
          </Col>
          <Col md={3} className="text-center">
            <Button variant="primary" size="lg" className="mb-3 mybutton" href="Map.html">Travaillez avec nous</Button>
          </Col>
        </Row>
      </Container>

      <footer className="bg-dark text-white py-4">
        <Container>
          <Row>
            <Col md={6}>
              <h5>À propos de nous</h5>
              <p>Nous sommes une plateforme dédiée à l'organisation et à l'optimisation de vos voyages. Notre équipe travaille sans relâche pour vous offrir des itinéraires sur mesure, des recommandations locales et un suivi en temps réel pour que vous puissiez voyager en toute sérénité.</p>
            </Col>
            <Col md={6}>
              <h5>Nous contacter</h5>
              <ul className="list-unstyled">
                <li><i className="fas fa-map-marker-alt"></i> Adresse: enspy, Yaounde</li>
                <li><i className="fas fa-phone"></i> Téléphone: +237 620 86 47 61</li>
                <li><i className="fas fa-envelope"></i> Email: contact@myway.com</li>
              </ul>
            </Col>
          </Row>
        </Container>
      </footer>
    </div>
  );
}

export default App;
