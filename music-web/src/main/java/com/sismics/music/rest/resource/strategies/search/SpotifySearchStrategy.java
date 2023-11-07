package com.sismics.music.rest.resource.strategies.search;

import javax.ws.rs.core.Response;

import com.sismics.music.core.service.spotify.SpotifyService;

public class SpotifySearchStrategy implements SearchStrategy {

    @Override
    public Response search(String query) {
        final SpotifyService spotifyService = new SpotifyService();
        String api_token = spotifyService.getToken();
        System.out.println(api_token);
        String responseBody = spotifyService.searchTracks(api_token, query);
        return Response.ok(responseBody).build();
    }
}
