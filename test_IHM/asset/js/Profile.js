
// Sélectionnez les boutons et les sections de contenu
const settingsBtn = document.getElementById('Settings');
const itinerariesBtn = document.getElementById('Itineraries');
const placesBtn = document.getElementById('Places');

const settingsSection = document.getElementById('settingsSection');
const itinerariesSection = document.getElementById('itinerariesSection');
const placesSection = document.getElementById('placesSection');

// Fonction pour cacher toutes les sections
function hideAllSections() {
    settingsSection.style.display = 'none';
    itinerariesSection.style.display = 'none';
    placesSection.style.display = 'none';
}

// Fonction pour désactiver tous les boutons
function deactivateAllButtons() {
    settingsBtn.classList.remove('active');
    itinerariesBtn.classList.remove('active');
    placesBtn.classList.remove('active');
}

// Fonction pour gérer les clics sur les boutons
function handleButtonClick(event) {
    // Cachez toutes les sections et désactivez tous les boutons
    hideAllSections();
    deactivateAllButtons();

    // Identifiez le bouton cliqué
    const clickedBtn = event.target;

    // Affichez la section et activez le bouton cliqué
    if (clickedBtn === settingsBtn) {
        settingsSection.style.display = 'block';
        settingsBtn.classList.add('active');
    } else if (clickedBtn === itinerariesBtn) {
        itinerariesSection.style.display = 'block';
        itinerariesBtn.classList.add('active');
    } else if (clickedBtn === placesBtn) {
        placesSection.style.display = 'block';
        placesBtn.classList.add('active');
    }
}

// Ajoutez les écouteurs d'événements aux boutons
settingsBtn.addEventListener('click', handleButtonClick);
itinerariesBtn.addEventListener('click', handleButtonClick);
placesBtn.addEventListener('click', handleButtonClick);

// Par défaut, affichez la section settings et activez le bouton settings
hideAllSections();
settingsSection.style.display = 'block';
settingsBtn.classList.add('active');
