package com.sismics.music.core.model.dbi;

public class PlaylistDecorator extends BaseDecorator{
    private boolean type;

    public PlaylistDecorator(PlaylistInterface wrapper,boolean type) {
        super(wrapper);
        this.type=type;
    }

    public boolean getType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

}
