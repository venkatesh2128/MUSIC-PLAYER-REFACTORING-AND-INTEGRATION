package com.sismics.music.rest.resource.strategies.search;

import javax.ws.rs.core.Response;

import com.sismics.music.core.model.context.AppContext;
import com.sismics.music.core.service.lastfm.LastFmService;

public class LastFmSearchStrategy implements SearchStrategy {

    @Override
    public Response search(String query) {
        final LastFmService lastFmService = AppContext.getInstance().getLastFmService();
        String responseBody = lastFmService.searchTrack(query);
        return Response.ok(responseBody).build();
    }
}
