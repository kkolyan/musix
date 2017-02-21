package com.nplekhanov.musix;

import java.util.NavigableSet;

/**
 * Created by nplekhanov on 21/02/2017.
 */
public class Chart {
    private String name;
    private NavigableSet<String> tracks;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public NavigableSet<String> getTracks() {
        return tracks;
    }

    public void setTracks(NavigableSet<String> tracks) {
        this.tracks = tracks;
    }
}
