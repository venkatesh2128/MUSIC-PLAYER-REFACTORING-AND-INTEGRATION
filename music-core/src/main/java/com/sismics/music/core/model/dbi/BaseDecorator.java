package com.sismics.music.core.model.dbi;

import com.google.common.base.Objects;

public class BaseDecorator implements PlaylistInterface{
    protected  PlaylistInterface wrapper;
    public BaseDecorator(PlaylistInterface wrapper) {
        this.wrapper = wrapper;
    }

    @Override
    public String getId() {
        return wrapper.getId();
    }


    @Override
    public void setId(String id) {
        wrapper.setId(id);

    }
    @Override
    public String getUserId() {
        return wrapper.getUserId();
    }

    @Override
    public String getName() {
        return wrapper.getName();
    }

    @Override
    public void setName(String name) {
        wrapper.setName(name);

    }
    @Override
    public void setUserId(String userId) {
        wrapper.setUserId(userId);
    }

    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", wrapper.getId())
                .add("userId", wrapper.getUserId())
                .add("name", wrapper.getName())
                .toString();
    }
}
