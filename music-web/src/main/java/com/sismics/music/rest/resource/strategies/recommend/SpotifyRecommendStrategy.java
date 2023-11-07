package com.sismics.music.rest.resource.strategies.recommend;

import javax.ws.rs.core.Response;

import com.sismics.music.core.service.spotify.SpotifyService;

public class SpotifyRecommendStrategy implements RecommendationStrategy {
    @Override
    public Response recommend(String artist, String track) {
        // Utilize the spotify service to fetch recommendations
        final SpotifyService spotifyService = new SpotifyService();
        String api_token = spotifyService.getToken();
        String responseBody = spotifyService.recommendTracks(api_token, artist,
                track);
        return Response.ok(responseBody).build();
    }
}
