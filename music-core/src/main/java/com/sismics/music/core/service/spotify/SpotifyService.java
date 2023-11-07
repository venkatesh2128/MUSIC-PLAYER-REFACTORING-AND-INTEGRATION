package com.sismics.music.core.service.spotify;

import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonObject;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.StringReader;
import java.util.Base64;
import java.net.URISyntaxException;
import java.net.URLEncoder;

public class SpotifyService {
    public String getToken() {
        String clientID = "3fce170b11cf4e7097aad42f8de20572";
        String clientSecret = "716b77bf8ec94eaba258e1de39e1edca";
        String auth = clientID + ":" + clientSecret;
        String encodedCredentials = Base64.getEncoder().encodeToString(auth.getBytes());
        String authHeader = "Basic " + encodedCredentials;
        String requestBody = "grant_type=client_credentials";

        String apiUrl = "https://accounts.spotify.com/api/token";
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(apiUrl);

        try {
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            httpPost.setHeader("Authorization", authHeader);
            httpPost.setEntity(new StringEntity(requestBody));
            // Execute the API request and get the response
            HttpResponse response = httpClient.execute(httpPost);
            // Get the response body as a String
            String responseBody = EntityUtils.toString(response.getEntity());
            // Extract accesToken from response json
            JsonObject jsonObject = Json.createReader(new StringReader(responseBody)).readObject();
            String accessToken = jsonObject.getString("access_token");
            return accessToken;
        } catch (IOException | JsonException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String searchTracks(String token, String query) {
        String authHeader = "Bearer " + token;
        String encodedQuery = URLEncoder.encode(query);
        String apiUrl = "https://api.spotify.com/v1/search?type=track&q=" + encodedQuery;
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(apiUrl);

        try {
            httpGet.setHeader("Content-Type", "application/json");
            httpGet.setHeader("Authorization", authHeader);
            // Execute the API request and get the response
            HttpResponse response = httpClient.execute(httpGet);
            // Get the response body as a String
            String responseBody = EntityUtils.toString(response.getEntity());
            // System.out.println(responseBody);
            return responseBody;
        } catch (IOException | JsonException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String recommendTracks(String token, String seed_artists, String seed_tracks) {
        String authHeader = "Bearer " + token;
        System.out.println("From the service - " + seed_artists + " " + seed_tracks);
        seed_artists = URLEncoder.encode(seed_artists);
        seed_tracks = URLEncoder.encode(seed_tracks);
        String seed_genres = URLEncoder.encode("classical,pop,rock,chill,groove");

        HttpClient httpClient = HttpClients.createDefault();

        try {
            URIBuilder uriBuilder = new URIBuilder("https://api.spotify.com/v1/recommendations");
            uriBuilder.setParameter("seed_artists", seed_artists);
            uriBuilder.setParameter("seed_tracks", seed_tracks);
            uriBuilder.setParameter("seed_genre", seed_genres);
            uriBuilder.setParameter("limit", "5");
            HttpGet httpGet = new HttpGet(uriBuilder.build());

            httpGet.setHeader("Content-Type", "application/json");
            httpGet.setHeader("Authorization", authHeader);
            // Execute the API request and get the response
            HttpResponse response = httpClient.execute(httpGet);
            // Get the response body as a String
            String responseBody = EntityUtils.toString(response.getEntity());
            // System.out.println(responseBody);
            return responseBody;
        } catch (IOException | JsonException | URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }
}
