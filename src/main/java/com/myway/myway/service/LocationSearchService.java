package com.myway.myway.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

@Service
public class LocationSearchService {

    private final String searchUrl = "https://nominatim.openstreetmap.org/search?format=json&q=";

    public String searchLocation(String query) throws IOException {
        String encodedQuery = java.net.URLEncoder.encode(query, StandardCharsets.UTF_8.toString());
        String url = searchUrl + encodedQuery;

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                return EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            }
        }
    }
}
