package com.sismics.music.rest.resource.strategies.search;

import javax.ws.rs.core.Response;

public interface SearchStrategy {
    public Response search(String query);
}
