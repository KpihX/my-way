package com.myway.myway.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

@Service
public class RoutingService {

    private final String serviceUrl = "https://router.project-osrm.org/route/v1";

    public String calculateRoute(String travelMode, List<double[]> selectedPlaces) throws IOException {
        // Construire la chaîne de requête
        StringBuilder coordinates = new StringBuilder();
        for (double[] place : selectedPlaces) {
            coordinates.append(place[0]).append(",").append(place[1]).append(";");
        }
        // Supprimer le dernier point-virgule
        coordinates.setLength(coordinates.length() - 1);

        // Construire l'URL de la requête
        String url = serviceUrl + "/" + travelMode + "/" + coordinates.toString() + "?overview=full&geometries=geojson";

        // Faire la requête HTTP GET
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                return EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            }
        }
    }
}
