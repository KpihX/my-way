import React from 'react';
import { Carousel, Button } from 'react-bootstrap';
import './carrousel.css';

const Carrousel = () => {
  return (
    <Carousel>
      <Carousel.Item interval={1000}>
        <img className="d-block w-100 slide" src="content/images/carousel/img3.jpg" alt="First slide" />
        <Carousel.Caption className="myCaption">
          <h3>Voyagez en toute sérénité avec notre suivi d itinéraire en temps réel.</h3>
          <p>Planifiez et suivez chaque étape de votre voyage en toute confiance.</p>
          <Button variant="primary" href="/map">
            Visitez la carte
          </Button>
        </Carousel.Caption>
      </Carousel.Item>
      <Carousel.Item interval={500}>
        <img className="d-block w-100 slide" src="content/images/carousel/img7.jpg" alt="Second slide" />
        <Carousel.Caption className="myCaption">
          <h3>Explorez des itinéraires uniques pour des voyages mémorables.</h3>
          <p>Découvrez des destinations cachées et des parcours sur mesure.</p>
          <Button variant="primary" href="/register">
            Inscrivez vous ici
          </Button>
        </Carousel.Caption>
      </Carousel.Item>
      <Carousel.Item>
        <img className="d-block w-100 slide" src="content/images/carousel/img9.jpg" alt="Third slide" />
        <Carousel.Caption className="myCaption">
          <h3 className="myCaption1">Optimisez votre voyage avec des itinéraires personnalisés et faciles à suivre.</h3>
          <p className="myCaption1">Simplifiez vos déplacements avec de simples vues sur le trajet.</p>
          <Button variant="primary" href="/map">
            cliquer pour essayer
          </Button>
        </Carousel.Caption>
      </Carousel.Item>
    </Carousel>
  );
};

export default Carrousel;
