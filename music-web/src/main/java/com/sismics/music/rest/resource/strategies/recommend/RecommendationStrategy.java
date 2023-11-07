package com.sismics.music.rest.resource.strategies.recommend;

import javax.ws.rs.core.Response;

public interface RecommendationStrategy {
    public Response recommend(String artist, String track);
}
