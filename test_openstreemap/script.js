// Clé API OpenStreetMap
const API_KEY = "kQRiUWfXPkULKVLKWtekcAorSTMO21v2MU6UGETWdnw";

// Créer la carte
const map = L.map("mapid").setView([48.8566, 2.3488], 13);

// Ajouter les tuiles OpenStreetMap
L.tileLayer(`https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=${API_KEY}`, {
    attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors, <a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>',
    maxZoom: 18,
    id: 'mapbox.streets'
}).addTo(map);

// Fonction pour récupérer les coordonnées du clic
function onMapClick(e) {
    const latLng = e.latlng;
    console.log(`Coordonnees du clic: ${latLng.lat}, ${latLng.lng}`);
    // Vous pouvez utiliser les coordonnées ici pour d'autres traitements
}

// Ecouter l'événement 'click' sur la carte
map.on('click', onMapClick);
