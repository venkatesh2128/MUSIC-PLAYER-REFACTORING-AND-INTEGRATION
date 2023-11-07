package com.sismics.music.rest.resource.strategies.recommend;

import javax.ws.rs.core.Response;

import com.sismics.music.core.model.context.AppContext;
import com.sismics.music.core.service.lastfm.LastFmService;

public class LastFmRecommendStrategy implements RecommendationStrategy {
    @Override
    public Response recommend(String artist, String track) {
        // Utilize the lastfm service to fetch recommendations
        final LastFmService lastFmService = AppContext.getInstance().getLastFmService();
        String responseBody = lastFmService.recommendTracks(artist, track);
        return Response.ok(responseBody).build();
    }
}
