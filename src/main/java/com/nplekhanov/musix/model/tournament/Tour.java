package com.nplekhanov.musix.model.tournament;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nplekhanov on 21/06/2017.
 */
public class Tour {
    Map<String, Collection<String>> tracksByUser = new HashMap<>();

    public Map<String, Collection<String>> getTracksByUser() {
        return tracksByUser;
    }

    public void setTracksByUser(Map<String, Collection<String>> tracksByUser) {
        this.tracksByUser = tracksByUser;
    }
}
