package com.sismics.music.core.model.dbi;

public class PlaylistReadOnlyDecorator extends BaseDecorator {

    private String type;

    public PlaylistReadOnlyDecorator(PlaylistInterface wrapper) {
        super(wrapper);
        this.type="ReadOnly";
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
